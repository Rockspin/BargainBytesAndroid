package com.rockspin.bargainbits.ui.deal_choice

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.data.models.GameDeal
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.util.format.PriceFormatter
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_deal_choice.*
import kotlinx.android.synthetic.main.price_view.*
import javax.inject.Inject

class DealChoiceDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private val GAME_DEALS_KEY = "GAME_DEALS"

        fun newInstance(gameDeals: List<GameDeal>): DealChoiceDialogFragment {
            val fragment = DealChoiceDialogFragment()
            val b = Bundle()
            b.putSerializable(GAME_DEALS_KEY, ArrayList<GameDeal>(gameDeals))
            fragment.arguments = b
            return fragment
        }
    }

    lateinit @Inject var adapter: DealChoiceAdapter
    lateinit @Inject var priceFormatter: PriceFormatter
    lateinit @Inject var storeRepository: StoreRepository

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deal_choice, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val gameDeals = arguments.getSerializable(GAME_DEALS_KEY) as List<GameDeal>

        Observable.fromIterable(gameDeals)
            .flatMap { gameDeal ->
                storeRepository.getGameStoreForId(gameDeal.storeId).toObservable()
                    .subscribeOn(Schedulers.io())
                    .map {
                        DealChoiceModel(it.name, it.imageUrl,
                            salePrice = priceFormatter.formatPrice(gameDeal.salePrice),
                            retailPrice = priceFormatter.formatPrice(gameDeal.normalPrice))
                    }
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { dealChoiceList ->
                adapter.setDealChoiceModels(dealChoiceList)
            }

        storeOptionRecycler.adapter = adapter
    }
}