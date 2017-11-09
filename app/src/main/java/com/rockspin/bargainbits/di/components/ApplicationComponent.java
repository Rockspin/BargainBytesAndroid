package com.rockspin.bargainbits.di.components;

import com.rockspin.bargainbits.di.modules.application.ApplicationModule;
import com.rockspin.bargainbits.BargainBytesApp;
import com.rockspin.bargainbits.di.modules.APIServiceModule;
import com.rockspin.bargainbits.di.modules.AndroidActivityModule;
import com.rockspin.bargainbits.di.modules.CacheModule;
import com.rockspin.bargainbits.di.modules.FormattingModule;
import com.rockspin.bargainbits.di.modules.JsonSerialisationModule;
import com.rockspin.bargainbits.di.modules.RepositoryModule;
import com.rockspin.bargainbits.di.modules.SchedulersModule;
import com.rockspin.bargainbits.di.modules.UrlModule;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ApplicationModule.class,
    APIServiceModule.class,
    JsonSerialisationModule.class,
    CacheModule.class,
    UrlModule.class,
    SchedulersModule.class,
    FormattingModule.class,
    RepositoryModule.class,
    AndroidInjectionModule.class,
    AndroidActivityModule.class
})
public interface ApplicationComponent {
    void inject(BargainBytesApp bargainBytesApp);
}
