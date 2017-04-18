package com.rockspin.bargainbits.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.fernandocejas.arrow.checks.Preconditions;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @param <View> The presenter view this presenter will be updating.
 * @param <Model> the model this class uses to fetch data.
 * @param <Data> the initial data this presenter needs on viewWillShow.
 */
public abstract class BasePresenter<View, Model, Data> {

    @Nullable protected final Model model;
    private boolean started = false;
    protected View view;

    @Nullable private CompositeSubscription presenterSubscriptions;

    protected BasePresenter() {
        model = null;
    }

    protected BasePresenter(@NonNull final Model model) {
        this.model = model;
    }

    public void setData(Data v) {
    }

    @CallSuper public void start(Data data, View view) {
        Preconditions.checkNotNull(data, "data cannot be null");
        start(view);
    }

    @CallSuper public void start(View view) {
        this.view = view;
        Preconditions.checkArgument(!started, "presenter must be stopped before it can be started.");
        Preconditions.checkArgument(presenterSubscriptions == null, "existing subscriptions");
        presenterSubscriptions = new CompositeSubscription();
        started = true;
    }

    @CallSuper public void stop() {
        Preconditions.checkArgument(started, "presenter must be started before it can be stopped");
        Preconditions.checkArgument(presenterSubscriptions != null, "presenterSubscriptions cannot be null");
        Preconditions.checkArgument(!presenterSubscriptions.isUnsubscribed(), "subscriptions cannot be unsubscribed");
        presenterSubscriptions.unsubscribe();
        presenterSubscriptions = null;
        started = false;
    }

    /**
     * Add a subscription to be unsubscribed when viewWillHide is called in this presenter.
     *
     * @param subscription subscription to be unsubscribed.
     * @return the subscription to be unsubscribed.
     */
    protected Subscription addSubscription(@NonNull Subscription subscription) {
        Preconditions.checkArgument(presenterSubscriptions != null, "presenterSubscriptions cannot be null");
        Preconditions.checkArgument(!presenterSubscriptions.isUnsubscribed(), "adding to unsubscribed Subscription in presenter");
        presenterSubscriptions.add(subscription);
        return subscription;
    }

    /**
     * Get a model.
     *
     * @return model for this presenter, will throw an exception of no model exists.
     */
    public Model getModel() {
        return Preconditions.checkNotNull(model, "Model cannot be null");
    }

    public View getView() {
        return view;
    }
}
