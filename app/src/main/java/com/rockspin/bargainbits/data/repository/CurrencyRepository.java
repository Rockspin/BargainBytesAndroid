package com.rockspin.bargainbits.data.repository;

import com.rockspin.bargainbits.data.models.currency.BBCurrency;
import com.rockspin.bargainbits.data.models.currency.CurrencyHelper;
import java.util.Currency;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * @deprecated The currency change feature is hardly used in the app. Remove it from the code
 */
@Singleton
@Deprecated
public class CurrencyRepository {

    @Inject public CurrencyRepository() {
        // load the default currency
    }

    public String getActiveCurrencySymbol() {
        return Currency.getInstance("USD").getSymbol();
    }

    /**
     * Emits a {@link CurrencyHelper} that is used to convert values to the currently active currency.
     */
    public Observable<CurrencyHelper> onCurrentCurrencyChanged() {
        return Observable.just(new CurrencyHelper(new BBCurrency("USD", 1.0f)));
    }
}
