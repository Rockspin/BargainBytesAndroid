package com.rockspin.bargainbits.deals

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.rockspin.bargainbits.R

class DealsActivity : AppCompatActivity() {

    @BindView(R.id.deals_recycler)
    lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: DealsViewModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.actvity_deals)

        viewModel = ViewModelProviders.of(this).get(DealsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        recyclerView
    }
}