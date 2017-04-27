package com.rockspin.bargainbits.ui.search.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ActivitySearchDetailBinding
import com.rockspin.bargainbits.ui.BaseMvpActivity
import com.rockspin.bargainbits.util.visible
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat


class SearchDetailActivity : BaseMvpActivity<SearchDetailPresenter.View, SearchDetailPresenter>(), SearchDetailPresenter.View {

    @Inject override lateinit var presenter: SearchDetailPresenter
    @Inject lateinit var adapter: AbbreviatedDealAdapter

    private lateinit var binding: ActivitySearchDetailBinding
    private val addToWatchListClick = PublishSubject.create<Unit>()
    private var menuClickDisposable: Disposable? = null

    companion object {
        private val GAME_ID_KEY = "GAME_ID_KEY"
        private val GAME_NAME_KEY = "GAME_NAME_KEY"

        fun createIntent(context: Context, gameId: String, gameName: String): Intent {
            return Intent(context, SearchDetailActivity::class.java)
                .putExtra(GAME_ID_KEY, gameId)
                .putExtra(GAME_NAME_KEY, gameName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_detail)

        setSupportActionBar(binding.searchDetailToolbar.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.dealResultsRecyclerView.adapter = adapter

        // TODO - Handle Config Changes
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter.setData(intent.getStringExtra(GAME_ID_KEY), intent.getStringExtra(GAME_NAME_KEY))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_detail, menu)
        // TODO - there must be a better way to do this
        menuClickDisposable = menu!!.findItem(R.id.action_add_to_watch_list).clicks()
            .subscribe { addToWatchListClick.onNext(Unit) }
        return true
    }

    override fun onDestroy() {
        menuClickDisposable?.dispose()
        super.onDestroy()
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
        adapter.viewModels = deals
    }

    override fun showLoadError() {
        Toast.makeText(this, "Error loading search detail", Toast.LENGTH_SHORT).show()
        // TODO - show try again view
    }

    override fun openDealUrl(url: String) {
        val uri = Uri.parse(url)

        // create an intent builder
        val intentBuilder = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.primary_color))
            .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.primary_color_dark))
            .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
            .setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
            .enableUrlBarHiding()

        val customTabsIntent = intentBuilder.build()
        customTabsIntent.launchUrl(this, uri)
    }

    override val onItemClicked: Observable<Int>
        get() = adapter.onItemClicked

    override val onWatchListClicked: Observable<*>
        get() = addToWatchListClick

    //endregion
}