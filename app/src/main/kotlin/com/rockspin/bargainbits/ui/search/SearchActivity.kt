package com.rockspin.bargainbits.ui.search

import android.app.SearchManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ActivitySearchBinding
import com.rockspin.bargainbits.ui.BaseMvpActivity
import com.rockspin.bargainbits.util.visible
import javax.inject.Inject

/**
 * Created by valentin.hinov on 22/04/2017.
 */
class SearchActivity : BaseMvpActivity<SearchPresenter.SearchView, SearchPresenter>(), SearchPresenter.SearchView {

    @Inject override lateinit var presenter: SearchPresenter

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.searchQuery = intent.getStringExtra(SearchManager.QUERY)
    }

    override fun showNoResults() {
        binding.searchResultsRecyclerView.visible = false
        binding.noResultsView.visible = true
    }

    override fun updateResults(viewModels: List<ResultViewModel>) {
        binding.noResultsView.visible = false
        binding.searchResultsRecyclerView.visible = true


    }

    override fun showFetchError() {
        
    }
}