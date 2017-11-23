package com.rockspin.bargainbits.deals

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import butterknife.BindView
import butterknife.ButterKnife
import com.rockspin.bargainbits.R
import java.util.*

class DealsActivity : AppCompatActivity() {

    @BindView(R.id.deals_recycler)
    lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: DealsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.actvity_deals)

        ButterKnife.bind(this)

        viewModel = ViewModelProviders.of(this).get(DealsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        val dealsAdapter = DealsAdapter(LayoutInflater.from(this))
        recyclerView.adapter = dealsAdapter

        dealsAdapter.setItems(Arrays.asList(Game("1", "Company of heroes", "", Arrays.asList(Deal("1", 23.00, 19.99, 10.00)))))
    }
}