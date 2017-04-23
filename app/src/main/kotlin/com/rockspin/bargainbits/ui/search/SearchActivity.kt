package com.rockspin.bargainbits.ui.search

import android.app.SearchManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ActivitySearchBinding
import com.rockspin.bargainbits.ui.BaseMvpActivity
import com.rockspin.bargainbits.ui.search.detail.SearchDetailBottomSheetDialogFragment
import com.rockspin.bargainbits.util.visible
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by valentin.hinov on 22/04/2017.
 */
class SearchActivity : BaseMvpActivity<SearchPresenter.SearchView, SearchPresenter>(), SearchPresenter.SearchView {

    @Inject override lateinit var presenter: SearchPresenter
    @Inject lateinit var adapter: SearchResultAdapter

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.searchResultsRecyclerView.adapter = adapter

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter.searchQuery = intent.getStringExtra(SearchManager.QUERY)
    }

    //region - SearchView interface

    override val backClick: Observable<*>
        get() = binding.backButton.clicks()

    override val resultClick: Observable<Int>
        get() = adapter.onItemSelected

    override fun showLoading(show: Boolean) {
        binding.searchProgressBar.visible = show
    }

    override fun showNoResults() {
        binding.searchResultsRecyclerView.visible = false
        binding.noResultsView.visible = true
    }

    override fun updateResults(viewModels: List<ResultViewModel>) {
        binding.noResultsView.visible = false
        binding.searchResultsRecyclerView.visible = true

        adapter.viewModels = viewModels
    }

    override fun showSearchDetail(gameId: String, gameName: String) {
        val searchDetailFragment = SearchDetailBottomSheetDialogFragment.newInstance(gameId, gameName)
        searchDetailFragment.show(supportFragmentManager, "SearchDetail")
    }

    override fun showFetchError() {
        // TODO
    }

    override fun goBack() {
        finish()
    }

    //endregion
}