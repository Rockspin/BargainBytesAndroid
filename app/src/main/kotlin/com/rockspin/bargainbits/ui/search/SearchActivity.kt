package com.rockspin.bargainbits.ui.search

import android.app.SearchManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ActivitySearchBinding
import com.rockspin.bargainbits.ui.BaseMvpActivity
import com.rockspin.bargainbits.ui.search.detail.SearchDetailActivity
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

        setSupportActionBar(binding.searchToolbar!!.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.searchResultsRecyclerView.adapter = adapter

        // TODO - Handle Config Changes

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter.searchQuery = intent.getStringExtra(SearchManager.QUERY)
    }

    //region - SearchView interface

    override val onBackClick: Observable<*>
        get() = binding.backButton.clicks()

    override val onResultClick: Observable<Int>
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
        startActivity(SearchDetailActivity.createIntent(this, gameId, gameName))
    }

    override fun showFetchError() {
        Toast.makeText(this, "Error loading search results", Toast.LENGTH_SHORT).show()
        // TODO - Show try again view
    }

    override fun goBack() {
        finish()
    }

    //endregion
}