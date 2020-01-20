package com.prikshit.delivery.ui.deliveries

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.prikshit.delivery.R
import com.prikshit.delivery.ui.deliveries.adapters.DeliveryAdapter
import com.prikshit.delivery.ui.deliveries.paging.Status
import com.prikshit.delivery.ui.deliveries.viewmodel.DeliveryViewmodel
import com.prikshit.delivery.ui.deliveryDetail.DeliveryDetailActivity
import com.prikshit.delivery.ui.factory.ViewModelFactory
import com.prikshit.domain.entities.DeliveryEntity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class DeliveryListActivity : AppCompatActivity(), DeliveryAdapter.DeliveryClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var deliveryVM: DeliveryViewmodel

    private val deliveryListAdapter = DeliveryAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        supportActionBar?.setTitle(R.string.title_deliveries)
        initRecyclerView()
        initDataSource()
        initBoundryCallbacks()
        deliveryListAdapter.retryLive.observe(this, Observer {
            deliveryVM.retry()
        })
        retryBtn.setOnClickListener {
            retryBtn.visibility = View.GONE
            deliveryVM.refreshList()
        }
    }

    private fun initDataSource() {
        deliveryVM = ViewModelProviders.of(this, viewModelFactory)
            .get(DeliveryViewmodel::class.java)
        swipeRefresh.setOnRefreshListener {
            deliveryVM.refreshList()
        }
        deliveryVM.deliveryListSource.observe(this, Observer {
            deliveryListAdapter.submitList(it)
            deliveryListAdapter.notifyDataSetChanged()
            hideRefresher()
        })
    }

    private fun initBoundryCallbacks() {
        deliveryVM.boundaryCallback.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> {
                    showRefresher()
                }
                Status.ERROR, Status.NETWORK_ERROR -> {
                    hideRefresher()
                    if (deliveryListAdapter.itemCount < 1) {
                        retryBtn.visibility = View.VISIBLE
                    }
                    deliveryListAdapter.setLoading(false)
                    var err = getString(
                        if (it == Status.NETWORK_ERROR) R.string.network_error
                        else R.string.error_message
                    )
                    deliveryListAdapter.showRetry(true, err)
                    Snackbar.make(root, err, Snackbar.LENGTH_LONG).show()
                    hideRefresher()
                }
                Status.PAGE_LOADING -> {
                    hideRefresher()
                    deliveryListAdapter.setLoading(true)
                }
                Status.LOADED -> {
                    hideRefresher()
                    retryBtn.visibility = View.GONE
                    deliveryListAdapter.setIsLastItem(true)
                }
                else -> {
                    hideRefresher()
                    retryBtn.visibility = View.GONE
                    deliveryListAdapter.setLoading(loading = false)
                }
            }
        })
    }

    private fun initRecyclerView() {
        rvDeliveryList.layoutManager = LinearLayoutManager(this)
        rvDeliveryList.setHasFixedSize(true)
        rvDeliveryList.adapter = deliveryListAdapter
    }

    private fun hideRefresher() {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
    }

    private fun showRefresher() {
        swipeRefresh.isRefreshing = true
    }

    override fun onDeliveryTapped(delivery: DeliveryEntity) {
        startActivity(Intent(this, DeliveryDetailActivity::class.java).apply {
            putExtra("dId", delivery.id)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return false
    }
}
