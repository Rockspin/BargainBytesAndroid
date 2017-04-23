package com.rockspin.bargainbits.ui.search.detail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.FragmentSearchDetailBinding

/**
 * Shows more information about a game search result
 */
class SearchDetailBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private val GAME_ID_KEY = "GAME_ID_KEY"
        private val GAME_NAME_KEY = "GAME_NAME_KEY"

        fun newInstance(gameId: String, gameName: String): SearchDetailBottomSheetDialogFragment {
            val bundle = Bundle(2)
            bundle.putString(GAME_ID_KEY, gameId)
            bundle.putString(GAME_NAME_KEY, gameName)

            val fragment = SearchDetailBottomSheetDialogFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var binding: FragmentSearchDetailBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gameTitle.text = arguments.getString(GAME_NAME_KEY)
    }
}