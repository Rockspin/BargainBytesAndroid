package com.rockspin.bargainbits.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rockspin.apputils.di.annotations.ApplicationScope;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * network utils class.
 */
public class NetworkUtils {

    private final Context context;
    private final BehaviorSubject<Boolean> behaviorSubject = BehaviorSubject.create();

    @Inject NetworkUtils(@ApplicationScope Context context) { /* not used */
        this.context = context;
        behaviorSubject.onNext(isConnectedToInternet());
    }

    /**
     * Check if we are connected to the internet.
     * @return if the phone currently has an internet connection
     */
    public boolean isConnectedToInternet() {
        return isConnectedToInternet(context);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            behaviorSubject.onNext(isConnectedToInternet());
        }
    };

    public Observable<Boolean> onNetworkChanged() {
        return behaviorSubject.doOnSubscribe(__ -> register())
                              .doOnDispose(this::unregister);
    }

    private void register() {
        context.registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unregister() {
        context.unregisterReceiver(broadcastReceiver);
    }

    private static boolean isConnectedToInternet(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
