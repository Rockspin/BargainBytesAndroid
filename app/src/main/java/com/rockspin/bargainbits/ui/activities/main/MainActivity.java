package com.rockspin.bargainbits.ui.activities.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.fernandocejas.arrow.checks.Preconditions;
import com.jakewharton.rxbinding.support.v4.view.RxViewPager;
import com.jakewharton.rxbinding.support.v4.widget.RxDrawerLayout;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.currency.CurrencyNamesAndISOCodes;
import com.rockspin.bargainbits.ui.activities.WatchListActivity;
import com.rockspin.bargainbits.ui.activities.main.deals.DealsFragmentPagerAdapter;
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.StoresDrawerFragment;
import com.rockspin.bargainbits.ui.dialogs.CurrencyPickerDialogFragment;
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListViewImpl;
import com.rockspin.bargainbits.utils.Feedback;
import javax.inject.Inject;
import org.codechimp.apprater.AppRater;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

import static com.rockspin.bargainbits.utils.RXTools.toVoid;

public class MainActivity extends BaseActivity implements DealsListViewImpl.DealsListContainer, MainActivityPresenter.IView {

    private static final String CURRENCY_DIALOG_TAG = "CURRENCY_DIALOG_TAG";
    private final PublishSubject<Integer> onOptionsItemSelected = PublishSubject.create();
    // used to send the options item clicks to the view
    private ActionBarDrawerToggle drawerToggle;

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.pager) ViewPager viewPager;
    @Bind(R.id.tabs) TabLayout tabLayout;

    @Bind(R.id.drawerViewParent) View drawerView;
    private AppCompatSpinner appCompatSpinner;
    @Inject MainActivityPresenter presenter;

    private @Nullable Snackbar noInternetSnackbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_main);
        ButterKnife.bind(this);

        DealsFragmentPagerAdapter dealsFragmentPagerAdapter = new DealsFragmentPagerAdapter(getSupportFragmentManager(), getResources());

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_activity_all_deals);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.store_filter, R.string.title_activity_all_deals);
        drawerLayout.setDrawerListener(drawerToggle);

        //setup the TabLayout, the scrolling behaviour is controlled in xml
        final int color = ContextCompat.getColor(this, R.color.text_icon_color);
        tabLayout.setTabTextColors(Color.argb(178, Color.red(color), Color.green(color), Color.blue(color)), color);

        // TODO: adds a small shadow to the drawer, check if we can replace this with action bar
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Link view pager and the tab layout.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // add scrolling fragments of deals
        viewPager.setAdapter(dealsFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        appCompatSpinner = new AppCompatSpinner(this);

        // Set navigation layout.
        final ArrayAdapter sectionsSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.deal_list_type, R.layout.actionbar_spinner_textview);
        sectionsSpinnerAdapter.setDropDownViewResource(R.layout.dropdown_item_1line);
        appCompatSpinner.setAdapter(sectionsSpinnerAdapter);

        if (savedInstanceState == null) {
            //add the fragment that represents the drawer content
            final StoresDrawerFragment fragment = new StoresDrawerFragment();
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.drawerViewParent, fragment)
                                       .commit();
        }
        setUseNavigationSpinner(isPortrait());
    }

    @Override protected void onStart() {
        super.onStart();
        presenter.start(this);
    }

    @Override protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
        //TODO: look at this
        setUseNavigationSpinner(isPortrait());
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.deals, menu);

        // Associate searchable configuration with the Game SearchView
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_games));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        onOptionsItemSelected.onNext(item.getItemId());

        return super.onOptionsItemSelected(item);
    }

    @Override public void closeView() {
        finish();
    }

    private void setUseNavigationSpinner(boolean navigationSpinner) {
        if (navigationSpinner) {
            toolbar.removeView(appCompatSpinner);
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            toolbar.addView(appCompatSpinner);
            tabLayout.setVisibility(View.GONE);
        }
    }

    // MainActivityPresenter.View implementation

    @Override public Observable<Boolean> onDrawerOpened() {
        return RxDrawerLayout.drawerOpen(drawerLayout, GravityCompat.START);
    }

    @Override public Observable<Void> onOpenWatchListPressed() {
        return onOptionsItemSelected.compose(filterByID(R.id.action_watch_list));
    }

    @Override public Observable<Void> onStoresFilterPressed() {
        return onOptionsItemSelected.compose(filterByID(R.id.action_store_filter));
    }

    @Override public Observable<Void> onSelectCurrencyPressed() {
        return onOptionsItemSelected.compose(filterByID(R.id.action_displayed_currency));
    }

    @Override public Observable<Void> onRateAppPressed() {
        return onOptionsItemSelected.compose(filterByID(R.id.rate));
    }

    @Override public Observable<Void> onShareAppPressed() {
        return onOptionsItemSelected.compose(filterByID(R.id.share));
    }

    @Override public Observable<Void> onFeedbackPressed() {
        return onOptionsItemSelected.compose(filterByID(R.id.feedback));
    }

    @Override public Observable<Integer> onNavigationItemSelected() {
        return RxAdapterView.itemSelections(appCompatSpinner);
    }

    @Override public Observable<Integer> onViewPagerChanged() {
        return RxViewPager.pageSelections(viewPager);
    }

    @Override public void showCurrencyDisclaimer() {
        showSnackBar(R.string.currency_disclaimer);
    }

    @Override public void showShareAppDialog() {
        final Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject));
        final String shareText = getString(R.string.share_app_text, "http://play.google.com/store/apps/details?id=" + getPackageName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(shareIntent);
    }

    @Override public void errorLoadingCurrencies(Throwable throwable) {
        Timber.e(throwable, "Failure when retrieving currency exchange object");
        Snackbar.make(drawerLayout, R.string.error_no_currency_exchange, Snackbar.LENGTH_SHORT)
                .setActionTextColor(ContextCompat.getColor(this, R.color.primary_color))
                .show();
    }

    @Override public void showSelectCurrencyDialog(CurrencyNamesAndISOCodes currencyNamesAndISOCodes) {
        Preconditions.checkNotNull(currencyNamesAndISOCodes, "names and ios");
        CurrencyPickerDialogFragment.instantiate(currencyNamesAndISOCodes).show(getSupportFragmentManager(), CURRENCY_DIALOG_TAG);
    }

    @Override public void setStoresDrawerOpen(boolean open) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override public void selectTab(Integer integer) {
        viewPager.setCurrentItem(integer);
        tabLayout.setScrollPosition(integer, 0, true);
    }

    @Override public void setNavigationSpinnerItem(Integer integer) {
        appCompatSpinner.setSelection(integer);
    }

    @Override
    public void showNoInternetMessage() {
        noInternetSnackbar = Snackbar.make(viewPager, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE);
        noInternetSnackbar.show();
    }

    @Override
    public void hideNoInternetMessage() {
        if (noInternetSnackbar != null) {
            noInternetSnackbar.dismiss();
            noInternetSnackbar = null;
        }
    }

    @Override public void openWatchList() {
        final Intent watchListIntent = new Intent(this, WatchListActivity.class);
        startActivity(watchListIntent);
    }

    @Override public void goToStoreAndRate() {
        AppRater.rateNow(this);
    }

    @Override public void sendFeedbackEmail() {
        Feedback.sendEmailFeedback(this);
    }

    @Override public void setTitle(int title) {
        toolbar.setTitle(title);
    }

    private void showSnackBar(@StringRes int currency_disclaimer) {
        Snackbar.make(drawerView, currency_disclaimer, Snackbar.LENGTH_LONG).setActionTextColor(ContextCompat.getColor(this, R.color.primary_color)).show();
    }

    private static Observable.Transformer<Integer, Void> filterByID(int action_displayed_currency) {
        return integerObservable -> integerObservable.filter(id -> id == action_displayed_currency).map(toVoid());
    }
}