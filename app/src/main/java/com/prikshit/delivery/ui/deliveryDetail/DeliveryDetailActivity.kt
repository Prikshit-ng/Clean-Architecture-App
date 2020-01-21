package com.prikshit.delivery.ui.deliveryDetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.prikshit.delivery.R
import com.prikshit.delivery.databinding.ActivityDeliveryDetailBinding
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

    lateinit var deliveryDetailVM: DeliveryDetailViewmodel

    private var disposables = CompositeDisposable()

    private lateinit var binding: ActivityDeliveryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_detail)
        binding.lifecycleOwner = this
        val handlers = ClickHandler()
        binding.handlers = handlers
        init()
    }

    private fun init() {
        initTransitions()
        initToolbar()
        deliveryDetailVM = ViewModelProviders.of(this, viewModelFactory)
            .get(DeliveryDetailViewmodel::class.java)
        updateUI()
    }

    private fun initTransitions() {
        goodsIV.transitionName = intent.extras?.getString("TRANSITION_IMAGE")
        fromTV.transitionName = intent.extras?.getString("TRANSITION_FROM")
        toTV.transitionName = intent.extras?.getString("TRANSITION_TO")
        textView2.transitionName = intent.extras?.getString("TRANSITION_FROM1")
        textView.transitionName = intent.extras?.getString("TRANSITION_TO1")
    }

    private fun initToolbar() {
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun updateUI() {
        val deliveryDetail = deliveryDetailVM.detDeliveryById(intent.getStringExtra("dId") ?: "")
        binding.delivery = deliveryDetail
        var dnldImg = true
        deliveryDetail.observe(this, Observer {
            if (dnldImg)
                setUiData(it)
            dnldImg = false
        })
    }

    private fun setUiData(it: DeliveryEntity) {
        Glide.with(this)
            .load(it.goodsPicture)
            .error(getDrawable(R.drawable.ic_photo_black_24dp))
            .apply(RequestOptions().override(100, 100))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }
            })
            .into(goodsIV)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    inner class ClickHandler {

        fun onFavBtnClick(deliveryData: DeliveryEntity) {
            deliveryData.isFav = !deliveryData.isFav
            disposables.add(
                deliveryDetailVM.updateDelivery(deliveryData)
                    .subscribe({ },
                        { e -> e.printStackTrace() })
            )
        }
    }
}
