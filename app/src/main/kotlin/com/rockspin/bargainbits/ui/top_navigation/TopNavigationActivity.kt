package com.rockspin.bargainbits.ui.top_navigation

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ActivityTopNavigationBinding
import com.rockspin.bargainbits.ui.BaseActivity
import com.rockspin.bargainbits.ui.activities.main.deals.DealsFragmentPagerAdapter
import com.rockspin.bargainbits.ui.store_filter.StoreFilterDialogFragment
import com.rockspin.bargainbits.ui.watch_list.WatchListActivity
import com.rockspin.bargainbits.utils.Feedback
import com.rockspin.bargainbits.utils.NetworkUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.codechimp.apprater.AppRater
import javax.inject.Inject

class TopNavigationActivity : BaseActivity() {

    @Inject lateinit var networkUtils: NetworkUtils

    private var noInternetSnackbar: Snackbar? = null
    private val disposable = CompositeDisposable()

    private val binding by lazy { DataBindingUtil.setContentView<ActivityTopNavigationBinding>(this, R.layout.activity_top_navigation) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup the TabLayout, the scrolling behaviour is controlled in xml
        val color = ContextCompat.getColor(this, R.color.text_icon_color)
        binding.tabLayout.setTabTextColors(Color.argb(178, Color.red(color), Color.green(color), Color.blue(color)), color)

        binding.viewPager.adapter = DealsFragmentPagerAdapter(supportFragmentManager, resources)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.toolbar.inflateMenu(R.menu.top_navigation)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = MenuItemCompat.getActionView(binding.toolbar.menu.findItem(R.id.menu_search_games)) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        binding.toolbar.itemClicks()
            .map { it.itemId }
            .subscribe {
                when (it) {
                    R.id.menu_watch_list -> openWatchList()
                    R.id.menu_store_filter -> openStoresFilter()
                    R.id.menu_rate -> goToStoreAndRate()
                    R.id.menu_share -> showShareAppDialog()
                    R.id.menu_feedback -> sendFeedbackEmail()
                }
            }
            .addTo(disposable)

        networkUtils.onNetworkChanged()
            .subscribe { internetAvailable ->
                if (internetAvailable) {
                    hideNoInternetMessage()
                } else {
                    showNoInternetMessage()
                }
            }
            .addTo(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun openWatchList() {
        startActivity(Intent(this, WatchListActivity::class.java))
    }

    private fun openStoresFilter() {
        val storeFilterDialogFragment = StoreFilterDialogFragment.newInstance()
        storeFilterDialogFragment.show(supportFragmentManager, StoreFilterDialogFragment::class.java.simpleName)
    }

    private fun goToStoreAndRate() {
        AppRater.rateNow(this)
    }

    private fun sendFeedbackEmail() {
        Feedback.sendEmailFeedback(this)
    }

    private fun showShareAppDialog() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject))
        val shareText = getString(R.string.share_app_text, "http://play.google.com/store/apps/details?id=" + packageName)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(shareIntent)
    }

    private fun hideNoInternetMessage() {
        noInternetSnackbar?.dismiss()
        noInternetSnackbar = null
    }

    private fun showNoInternetMessage() {
        noInternetSnackbar = Snackbar.make(binding.viewPager, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
        noInternetSnackbar?.show()
    }
}