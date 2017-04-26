package com.rockspin.bargainbits.ui.search

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ActivitySearchDetailBinding
import com.rockspin.bargainbits.ui.BaseMvpActivity
import com.rockspin.bargainbits.ui.search.detail.AbbreviatedDealViewModel
import com.rockspin.bargainbits.ui.search.detail.SearchDetailPresenter
import com.rockspin.bargainbits.util.visible
import io.reactivex.Observable
import javax.inject.Inject

class SearchDetailActivity : BaseMvpActivity<SearchDetailPresenter.View, SearchDetailPresenter>(), SearchDetailPresenter.View {

    @Inject override lateinit var presenter: SearchDetailPresenter

    private lateinit var binding: ActivitySearchDetailBinding
    private lateinit var addToWatchListClick: Observable<Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_detail)

        setSupportActionBar(binding.searchDetailToolbar.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO - Handle Config Changes
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_detail, menu)
        addToWatchListClick = menu!!.getItem(R.id.action_add_to_watch_list).clicks()
        return true
    }

    //region - View interface

    override fun setScreenTitle(title: String) {
        setTitle(title)
    }

    override fun showLoading(show: Boolean) {
        binding.searchDetailProgressBar.visible = show
        binding.dealResultsRecyclerView.visible = !show
    }

    override fun showAvailableDeals(deals: List<AbbreviatedDealViewModel>) {
    }

    override fun showLoadError() {
        // TODO - show try again view
    }

    override val onItemClicked: Observable<Int>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val onWatchListClicked: Observable<*>
        get() = addToWatchListClick

    //endregion
}