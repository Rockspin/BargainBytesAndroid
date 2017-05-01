package com.rockspin.bargainbits.ui.top_navigation

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import butterknife.Bind
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.jakewharton.rxbinding2.support.v4.widget.RxDrawerLayout
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.ui.BaseMvpActivity
import com.rockspin.bargainbits.ui.activities.main.deals.DealsFragmentPagerAdapter
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.StoresDrawerFragment
import com.rockspin.bargainbits.ui.store_filter.StoreFilterDialogFragment
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListViewImpl
import com.rockspin.bargainbits.ui.watch_list.WatchListActivity
import com.rockspin.bargainbits.utils.Feedback
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import org.codechimp.apprater.AppRater
import javax.inject.Inject

class TopNavigationActivity : BaseMvpActivity<TopNavigationView, TopNavigationPresenter>(), TopNavigationView, DealsListViewImpl.DealsListContainer {

    private val onOptionsItemSelected = PublishSubject.create<Int>()

    @Inject override lateinit var presenter: TopNavigationPresenter

    @Bind(R.id.drawer_layout) internal lateinit var drawerLayout: DrawerLayout
    @Bind(R.id.toolbar) internal lateinit var toolbar: Toolbar
    @Bind(R.id.pager) internal lateinit var viewPager: ViewPager
    @Bind(R.id.tabs) internal lateinit var tabLayout: TabLayout

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var appCompatSpinner: AppCompatSpinner
    private var noInternetSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_top_navigation)
        ButterKnife.bind(this)

        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_drawer)
        setSupportActionBar(toolbar)

        supportActionBar?.setTitle(R.string.title_activity_all_deals)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout,
                R.string.store_filter, R.string.title_activity_all_deals)
        drawerLayout.addDrawerListener(drawerToggle)

        //setup the TabLayout, the scrolling behaviour is controlled in xml
        val color = ContextCompat.getColor(this, R.color.text_icon_color)
        tabLayout.setTabTextColors(Color.argb(178, Color.red(color), Color.green(color), Color.blue(color)), color)

        // TODO: adds a small shadow to the drawer, check if we can replace this with action bar
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START)

        viewPager.adapter = DealsFragmentPagerAdapter(supportFragmentManager, resources)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setupWithViewPager(viewPager)

        appCompatSpinner = AppCompatSpinner(this)

        // Set navigation layout.
        val sectionsSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.deal_list_type, R.layout.actionbar_spinner_textview)
        sectionsSpinnerAdapter.setDropDownViewResource(R.layout.dropdown_item_1line)
        appCompatSpinner.adapter = sectionsSpinnerAdapter

        if (savedInstanceState == null) {
            //add the fragment that represents the drawer content
            val fragment = StoresDrawerFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.layout_drawer, fragment)
                    .commit()
        }

        setUseNavigationSpinner(isPortrait())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_navigation, menu)

        // Associate searchable configuration with the GameSearchResult SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = MenuItemCompat.getActionView(menu?.findItem(R.id.menu_search_games)) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        onOptionsItemSelected.onNext(item?.itemId)

        return super.onOptionsItemSelected(item)
    }

    //region View methods
    override fun onDrawerToggled(): Observable<Boolean> {
        return RxDrawerLayout.drawerOpen(drawerLayout, GravityCompat.START)
    }

    override fun onOpenWatchListPressed(): Observable<Any> {
        return onOptionsItemSelected.compose(filterById(R.id.menu_watch_list))
    }

    override fun openWatchList() {
        startActivity(Intent(this, WatchListActivity::class.java))
    }

    override fun onStoresFilterPressed(): Observable<Any> {
        return onOptionsItemSelected.compose(filterById(R.id.menu_store_filter))
    }

    override fun openStoresDrawer() {
        val storeFilterDialogFragment = StoreFilterDialogFragment.newInstance()
        storeFilterDialogFragment.show(supportFragmentManager, StoreFilterDialogFragment::class.java.simpleName)
    }

    override fun onRateAppPressed(): Observable<Any> {
        return onOptionsItemSelected.compose(filterById(R.id.menu_rate))
    }

    override fun goToStoreAndRate() {
        AppRater.rateNow(this)
    }

    override fun onFeedbackPressed(): Observable<Any> {
        return onOptionsItemSelected.compose(filterById(R.id.menu_feedback))
    }

    override fun sendFeedbackEmail() {
        Feedback.sendEmailFeedback(this)
    }

    override fun onShareAppPressed(): Observable<Any> {
        return onOptionsItemSelected.compose(filterById(R.id.menu_share))
    }

    override fun showShareAppDialog() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject))
        val shareText = getString(R.string.share_app_text, "http://play.google.com/store/apps/details?id=" + packageName)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(shareIntent)
    }

    override fun onViewPagerChanged(): Observable<Int> {
        return RxViewPager.pageSelections(viewPager)
    }

    override fun setNavigationSpinnerItem(page: Int) {
        appCompatSpinner.setSelection(page)
    }

    override fun onNavigationItemSelected(): Observable<Int> {
        return RxAdapterView.itemSelections(appCompatSpinner)
    }

    override fun selectTab(item: Int) {
        viewPager.currentItem = item
        tabLayout.setScrollPosition(item, 0.0f, true)
    }

    override fun hideNoInternetMessage() {
        noInternetSnackbar?.dismiss()
        noInternetSnackbar = null
    }

    override fun showNoInternetMessage() {
        noInternetSnackbar = Snackbar.make(viewPager, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
        noInternetSnackbar?.show()
    }

    //endregion

    override fun closeView() {
        finish()
    }

    private fun isPortrait(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun setUseNavigationSpinner(navigationSpinner: Boolean) {
        if (navigationSpinner) {
            toolbar.removeView(appCompatSpinner)
            tabLayout.visibility = View.VISIBLE
        } else {
            toolbar.addView(appCompatSpinner)
            tabLayout.visibility = View.GONE
        }
    }

    private fun filterById(menuId: Int): ObservableTransformer<Int, Any> {
        return ObservableTransformer { upstream ->
            upstream.filter { id -> id == menuId }
                    .map { _ -> Any() }
        }
    }
}