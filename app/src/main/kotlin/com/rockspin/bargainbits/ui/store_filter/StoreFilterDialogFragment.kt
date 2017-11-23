package com.rockspin.bargainbits.ui.store_filter

import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.FragmentStoreFilterBinding
import com.rockspin.bargainbits.util.visible
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import javax.inject.Inject

class StoreFilterDialogFragment : BottomSheetDialogFragment(), StoreFilterPresenter.View {

    interface OnDialogClosedListener {
        fun onStoreFilterDialogClosed()
    }

    internal @Inject lateinit var presenter: StoreFilterPresenter
    internal @Inject lateinit var adapter: StoreFilterAdapter

    private lateinit var binding: FragmentStoreFilterBinding
    private var onDialogClosedListener: OnDialogClosedListener? = null

    companion object {
        fun newInstance(): StoreFilterDialogFragment {
            return StoreFilterDialogFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        onDialogClosedListener = context as? OnDialogClosedListener
    }

    override fun onDetach() {
        super.onDetach()
        onDialogClosedListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store_filter, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated(this)

        binding.storeFilterRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        presenter.onViewDestroyed()
        super.onDestroyView()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDialogClosedListener?.onStoreFilterDialogClosed()
    }

    //region - StoreFilterPresenter.View

    override fun showLoading(show: Boolean) {
        binding.storeFilterProgressBar.visible = show
        if (show) {
            binding.errorView.visible = false
        }
    }

    override fun showStoreList(storeList: List<StoreViewModel>) {
        adapter.showStoreList(storeList)
    }

    override fun updateStoreAtIndex(index: Int, viewModel: StoreViewModel) {
        adapter.updateStoreAtIndex(index, viewModel)
    }

    override fun showLoadError() {
        binding.errorView.visible = true
    }

    override val onItemToggled: Observable<Pair<Int, Boolean>>
        get() = adapter.onItemToggled

    override val onTryAgainClicked: Observable<Unit>
        get() = binding.tryAgainButton.clicks()

    //endregion
}