package com.rockspin.bargainbits.ui.deals

import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.jakewharton.rxbinding2.view.selected
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.jakewharton.rxbinding2.widget.itemSelections
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ActivityDealListBinding
import com.rockspin.bargainbits.ui.BaseActivity
import com.rockspin.bargainbits.ui.store_filter.StoreFilterDialogFragment
import com.rockspin.bargainbits.ui.watch_list.WatchListActivity
import com.rockspin.bargainbits.utils.Feedback
import com.rockspin.bargainbits.utils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.codechimp.apprater.AppRater
import javax.inject.Inject

class DealListActivity : BaseActivity() {

    @Inject lateinit var networkUtils: NetworkUtils
    @Inject lateinit var adapter: DealListAdapter
    @Inject lateinit var factory: DealListViewModelFactory

    private var noInternetSnackbar: Snackbar? = null
    private val disposable = CompositeDisposable()

    private val binding by lazy { DataBindingUtil.setContentView<ActivityDealListBinding>(this, R.layout.activity_deal_list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.deal_list)

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

        binding.dealsRecyclerView.adapter = adapter

        // TODO - move to view model
        networkUtils.onNetworkChanged()
            .subscribe { internetAvailable ->
                if (internetAvailable) {
                    hideNoInternetMessage()
                } else {
                    showNoInternetMessage()
                }
            }
            .addTo(disposable)

        val viewModel = ViewModelProviders.of(this, factory).get(DealListViewModel::class.java)

        binding.dealSortTypeSpinner
            .itemSelections()
            .subscribe {
                viewModel.sortIndex = it
            }

        viewModel.dealEntries
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { newEntries ->
                adapter.viewModels = newEntries
            }
            .addTo(disposable)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
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
        noInternetSnackbar = Snackbar.make(binding.root, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
        noInternetSnackbar?.show()
    }
}