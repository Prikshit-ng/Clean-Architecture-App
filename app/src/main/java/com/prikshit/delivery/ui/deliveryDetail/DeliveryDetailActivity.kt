package com.prikshit.delivery.ui.deliveryDetail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prikshit.delivery.R
import com.prikshit.delivery.ui.deliveryDetail.viewmodel.DeliveryDetailViewmodel
import com.prikshit.delivery.ui.factory.ViewModelFactory
import com.prikshit.domain.entities.DeliveryEntity
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_delivery_detail.*
import javax.inject.Inject

class DeliveryDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var deliveryDetailVM: DeliveryDetailViewmodel

    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_detail)
        init()
    }

    private fun init() {
        initToolbar()
        deliveryDetailVM = ViewModelProviders.of(this, viewModelFactory)
            .get(DeliveryDetailViewmodel::class.java)
        updateUI()
    }

    private fun initToolbar() {
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun updateUI() {
        var deliveryDetail = deliveryDetailVM.detDeliveryById(intent.getStringExtra("dId") ?: "")
        deliveryDetail.observe(this,
            Observer {
                setUiData(it)
                favButton.setOnClickListener { _ ->
                    var deliveryData = it
                    deliveryData.isFav = !it.isFav
                    disposables.add(
                        deliveryDetailVM.updateDelivery(deliveryData)
                            .subscribe({

                                deliveryDetail.removeObservers(this)
                                updateUI()
                            },
                                { e ->
                                    e.printStackTrace()
                                })
                    )
                }
            })
    }

    private fun setUiData(it: DeliveryEntity) {
        fromTV.text = it.routeEntity.start
        toTV.text = it.routeEntity.end
        remarksTV.text = it.remarks
        Glide.with(this)
            .load(it.goodsPicture)
            .error(getDrawable(R.drawable.ic_photo_black_24dp))
            .apply(RequestOptions().override(100, 100))
            .into(goodsIV)
        sendersName.text = it.senderEntity.name
        sendersPhone.text = it.senderEntity.phone
        sendersEmail.text = it.senderEntity.email
        deliveryFeeTV.text = it.deliveryFee
        surchargeFeeTV.text = it.surcharge
        totalFeeTV.text = it.getTotalAmount()
        if (it.isFav) {
            favButton.setText(R.string.remove_from_favourite)
            favButton.setIconResource(R.drawable.ic_favorite_fill)
        } else {
            favButton.setText(R.string.add_to_favourite)
            favButton.setIconResource(R.drawable.ic_favorite)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
