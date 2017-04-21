package com.rockspin.bargainbits.di.modules;

import com.rockspin.bargainbits.di.annotations.CheapsharkUrl;
import com.rockspin.bargainbits.di.annotations.CurrencyUrl;

import dagger.Module;
import dagger.Provides;

@Module
public class UrlModule {

    private static final String BASE_CURRENCY_URL = "https://api.fixer.io";
    private static final String BASE_CHEAPSHARK_URL = "https://www.cheapshark.com/api/1.0";

    @Provides @CheapsharkUrl public String cheapSharkrUrl(){
        return BASE_CHEAPSHARK_URL;
    }


    @Provides @CurrencyUrl public String currencyUrl(){
        return BASE_CURRENCY_URL;
    }

}
