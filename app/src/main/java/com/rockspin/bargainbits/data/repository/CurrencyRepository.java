package com.rockspin.bargainbits.data.repository;

import android.content.SharedPreferences;
import com.annimon.stream.Stream;
import com.rockspin.bargainbits.data.models.currency.BBCurrency;
import com.rockspin.bargainbits.data.models.currency.CurrencyExchange;
import com.rockspin.bargainbits.data.models.currency.CurrencyHelper;
import com.rockspin.bargainbits.data.models.currency.CurrencyNamesAndISOCodes;
import com.rockspin.bargainbits.data.rest_client.ICurrencyAPIService;
import com.rockspin.bargainbits.di.modules.SchedulersModule;
import com.rockspin.bargainbits.utils.Constants;
import com.rockspin.bargainbits.utils.RxSuccessCache;
import java.util.Currency;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Observable;
import rx.Scheduler;
import rx.subjects.BehaviorSubject;

@Singleton
public class CurrencyRepository {

    private final SharedPreferences sharedPreferences;
    private final ICurrencyAPIService iCurrencyAPIService;

    private final RxSuccessCache<CurrencyExchange> currencies = RxSuccessCache.create();
    private final BehaviorSubject<String> onISOCodeChanged;
    @Inject @Named(SchedulersModule.IO) Scheduler ioScheduler;
    @Inject @Named(SchedulersModule.MAIN) Scheduler mainScheduler;

    @Inject public CurrencyRepository(SharedPreferences sharedPreferences, ICurrencyAPIService iCurrencyAPIService) {
        this.sharedPreferences = sharedPreferences;
        this.iCurrencyAPIService = iCurrencyAPIService;
        // load the default currency
        this.onISOCodeChanged = BehaviorSubject.create(sharedPreferences.getString(Constants.SP_CURRENCY, Constants.DEFAULT_CURRENCY_CODE));
    }

    private Observable<CurrencyHelper> getCurrencies() {
        BBCurrency defaultCurrency = new BBCurrency(Constants.DEFAULT_CURRENCY_CODE, 1.0f);
        return currencies.get(() -> iCurrencyAPIService.getLatestUSD().subscribeOn(ioScheduler).observeOn(mainScheduler))
                         .flatMap(currencyExchange -> Observable.from(currencyExchange.getCurrencies()).startWith(defaultCurrency))
                         .onErrorResumeNext(Observable.just(defaultCurrency))
                         .map(CurrencyHelper::new);
    }

    public void setCurrencyIsoCode(String isoCode) {
        sharedPreferences.edit().putString(Constants.SP_CURRENCY, isoCode).apply();
        onISOCodeChanged.onNext(isoCode);
    }

    public String getActiveCurrencySymbol() {
        return Currency.getInstance(onISOCodeChanged.getValue()).getSymbol();
    }

    /**
     * Emits a {@link CurrencyHelper} that is used to convert values to the currently active currency.
     */
    public Observable<CurrencyHelper> onCurrentCurrencyChanged() {
        return onISOCodeChanged.distinctUntilChanged().flatMap(isoCode -> getCurrencies().first(currencyHelper -> currencyHelper.getIsoCOde().equals(isoCode)));
    }

    public Observable<CurrencyNamesAndISOCodes> getCurrencyNamesAndIsoCodes() {
        return getCurrencies().toList().map(currencyHelpers -> {
            final String[] isoNames = (String[]) Stream.of(currencyHelpers).map(CurrencyHelper::getIsoCOde).toArray();
            final String[] countryNames = (String[]) Stream.of(currencyHelpers).map(CurrencyHelper::getCountryName).toArray();
            return new CurrencyNamesAndISOCodes(isoNames, countryNames);
        });
    }

    public Observable<String> onIsoChanged() {
        return onISOCodeChanged;
    }
}
