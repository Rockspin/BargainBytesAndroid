package com.rockspin.bargainbits.ui.store_filter

import com.rockspin.bargainbits.data.repository.stores.filter.GameStoreFiltered
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter
import com.rockspin.bargainbits.ui.mvp.BaseMvpView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class StoreFilterPresenter @Inject constructor(private val storeFilter: StoreFilter): BaseMvpPresenter<StoreFilterPresenter.View>() {

    interface View: BaseMvpView {
        fun showLoading(show: Boolean)
        fun showStoreList(storeList: List<StoreViewModel>)
        fun showLoadError()

        val onItemToggled: Observable<Pair<Int, Boolean>>
        fun updateStoreAtIndex(index: Int, viewModel: StoreViewModel)

        val onTryAgainClicked: Observable<Unit>
    }

    private lateinit var loadedStoreList: List<GameStoreFiltered>

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        loadStoreList()

        addLifetimeDisposable(view.onItemToggled
            .filter { (index, isUsed) -> loadedStoreList[index].isUsed != isUsed }
            .subscribe({ (index, isUsed) ->
                val gameStore = loadedStoreList[index]
                val updatedStore = gameStore.withState(isUsed)

                storeFilter.updateStore(updatedStore)

                val mutableList = loadedStoreList.toMutableList()
                mutableList[index] = updatedStore
                loadedStoreList = mutableList

                this.view?.updateStoreAtIndex(index, viewModelFromGameStoreFiltered(updatedStore))
            }))

        addLifetimeDisposable(view.onTryAgainClicked
            .subscribe {
                loadStoreList()
            }
        )
    }

    private fun loadStoreList() {
        addLifetimeDisposable(storeFilter.getStoreList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { this.view?.showLoading(true) }
            .doFinally { this.view?.showLoading(false) }
            .doOnSuccess { loadedStoreList = it }
            .map { it.map { viewModelFromGameStoreFiltered(it) } }
            .subscribe({
                this.view?.showStoreList(it)
            }, {
                this.view?.showLoadError()
                Timber.e("Error loading stores", it)
            }))
    }

    private fun viewModelFromGameStoreFiltered(it: GameStoreFiltered) = StoreViewModel(it.storeName, it.imageUrl, it.isUsed)
}