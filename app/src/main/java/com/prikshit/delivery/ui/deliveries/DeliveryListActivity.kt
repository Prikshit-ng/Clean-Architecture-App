package com.prikshit.delivery.ui.deliveries

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
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
    private var viewPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
//        For maintaining shared element state
        if (savedInstanceState != null) {
            viewPosition = savedInstanceState.getInt("VIEW_POSITION")
        }
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                super.onMapSharedElements(names, sharedElements)
                if (sharedElements.isEmpty()) {
                    val view = rvDeliveryList.layoutManager?.findViewByPosition(viewPosition)
                    if (view != null) {
                        for (name in names) {
                            sharedElements[name] = getSharedView(name, view)
                        }
                    }
                }
            }
        })
    }

    fun getSharedView(nameZ: String, view: View): View {
        return when {
            nameZ.contains("from1") -> view.findViewById(R.id.textView2)
            nameZ.contains("from") -> view.findViewById(R.id.fromTV)
            nameZ.contains("to1") -> view.findViewById(R.id.textView)
            nameZ.contains("to") -> view.findViewById(R.id.toTV)
            else -> view.findViewById(R.id.imageView)
        }
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
            if (it.size != 0) {
                deliveryListAdapter.submitList(it)
                deliveryListAdapter.notifyDataSetChanged()
            }
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
                    deliveryListAdapter.showLoading(false)
                    val err = getString(
                        if (it == Status.NETWORK_ERROR) R.string.network_error
                        else R.string.error_message
                    )
                    deliveryListAdapter.showRetry(true, err)
                    Snackbar.make(root, err, Snackbar.LENGTH_LONG).show()
                    hideRefresher()
                }
                Status.PAGE_LOADING -> {
                    hideRefresher()
                    deliveryListAdapter.showLoading(true)
                }
                Status.LOADED -> {
                    hideRefresher()
                    retryBtn.visibility = View.GONE
                    deliveryListAdapter.setIsLastItem(true)
                }
                else -> {
                    hideRefresher()
                    retryBtn.visibility = View.GONE
                    deliveryListAdapter.showLoading(showLoader = false)
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

    override fun onDeliveryTapped(
        delivery: DeliveryEntity,
        arrOfViews: Array<View>,
        adapterPosition: Int
    ) {
        viewPosition = adapterPosition
        val intent = Intent(this, DeliveryDetailActivity::class.java).apply {
            putExtra("dId", delivery.id)
            putExtra("TRANSITION_IMAGE", ViewCompat.getTransitionName(arrOfViews[0]))
            putExtra("TRANSITION_FROM", ViewCompat.getTransitionName(arrOfViews[1]))
            putExtra("TRANSITION_TO", ViewCompat.getTransitionName(arrOfViews[2]))
            putExtra("TRANSITION_FROM1", ViewCompat.getTransitionName(arrOfViews[3]))
            putExtra("TRANSITION_TO1", ViewCompat.getTransitionName(arrOfViews[4]))
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            androidx.core.util.Pair.create(
                arrOfViews[0],
                ViewCompat.getTransitionName(arrOfViews[0]) ?: ""
            )
            ,
            androidx.core.util.Pair.create(
                arrOfViews[1],
                ViewCompat.getTransitionName(arrOfViews[1]) ?: ""
            ),
            androidx.core.util.Pair.create(
                arrOfViews[2],
                ViewCompat.getTransitionName(arrOfViews[2]) ?: ""
            ),
            androidx.core.util.Pair.create(
                arrOfViews[3],
                ViewCompat.getTransitionName(arrOfViews[3]) ?: ""
            ),
            androidx.core.util.Pair.create(
                arrOfViews[4],
                ViewCompat.getTransitionName(arrOfViews[4]) ?: ""
            )
        )
        startActivity(intent, options.toBundle())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("VIEW_POSITION", viewPosition)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return false
    }
}
