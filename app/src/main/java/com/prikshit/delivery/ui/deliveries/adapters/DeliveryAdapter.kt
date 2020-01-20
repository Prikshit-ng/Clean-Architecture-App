package com.prikshit.delivery.ui.deliveries.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_delivery.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prikshit.delivery.R
import com.prikshit.domain.entities.DeliveryEntity
import kotlinx.android.synthetic.main.item_end.view.*
import kotlinx.android.synthetic.main.item_loader.view.*
import java.lang.Exception

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1
const val VIEW_TYPE_END = 2

class DeliveryAdapter(private val listener: DeliveryClickListener) :
    PagedListAdapter<DeliveryEntity, RecyclerView.ViewHolder>(diffCallback) {

    private var showLoader = false
    private var retryObservable = false
    private var endObservable = false
    var retryLive = MutableLiveData<Boolean>()
    private var retryMsg = ""

    override fun getItemViewType(position: Int): Int {
        return when {
            position < super.getItemCount() -> VIEW_TYPE_ITEM
            endObservable -> VIEW_TYPE_END
            else -> VIEW_TYPE_LOADING
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasLoadingFooter()) 1 else 0
    }

    companion object {
        val diffCallback: DiffUtil.ItemCallback<DeliveryEntity> =
            object : DiffUtil.ItemCallback<DeliveryEntity>() {
                override fun areItemsTheSame(
                    oldItem: DeliveryEntity,
                    newItem: DeliveryEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: DeliveryEntity,
                    newItem: DeliveryEntity
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> return DeliveryVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_delivery, parent, false)
            )
            VIEW_TYPE_END -> return EndVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_end, parent, false)
            )
            else -> LoadingVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when {
            getItemViewType(position) == VIEW_TYPE_END -> (holder as EndVH).bind()
            getItemViewType(position) == VIEW_TYPE_LOADING -> (holder as LoadingVH).bind(showLoader, retryObservable)
            else -> try{
                (holder as DeliveryVH).bind(getItem(position) as DeliveryEntity)
            }catch (e:Exception){

            }
        }
    }

    inner class EndVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(){
            itemView.endChip.visibility = View.VISIBLE
            itemView.endTV.visibility = View.VISIBLE
            itemView.endChip.setOnClickListener {
                itemView.endChip.visibility = View.GONE
                itemView.endTV.visibility = View.GONE

            }
        }
    }

    inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(isLoading: Boolean, retry:Boolean) {
            itemView.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
            if(retry){
                itemView.retry.visibility = View.VISIBLE
                itemView.errorTV.visibility = View.VISIBLE
                itemView.errorTV.text = retryMsg
            }else{

                itemView.retry.visibility = View.GONE
                itemView.errorTV.visibility = View.GONE
            }
            itemView.retry.setOnClickListener {
                retryLive.postValue(true)
            }
        }
    }


    inner class DeliveryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(delivery: DeliveryEntity?) {
            delivery?.let {
                itemView.fromTV.text = delivery.routeEntity.start
                itemView.toTV.text = delivery.routeEntity.end
                if (delivery.isFav)
                    itemView.favIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.favIcon.context,
                            R.drawable.ic_favorite_fill
                        )
                    )
                else
                    itemView.favIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.favIcon.context,
                            R.drawable.ic_favorite
                        )
                    )
                itemView.amountTV.text = delivery.getTotalAmount()
                Glide.with(itemView.context)
                    .load(delivery.goodsPicture)
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(itemView.context.getDrawable(R.drawable.ic_photo_black_24dp))
                    .apply(RequestOptions().override(80, 80))
                    .into(itemView.imageView)

                itemView.setOnClickListener {
                    listener.onDeliveryTapped(delivery)
                }
            }
        }
    }

    override fun getItem(position: Int): DeliveryEntity? {
        return super.getItem(position)
    }

    fun setLoading(loading: Boolean) {
        this.endObservable = false
        this.showLoader = loading
        this.retryObservable = false
        notifyDataSetChanged()
    }

    fun showRetry(show:Boolean, msg:String){
        this.retryMsg = msg
        this.endObservable = false
        this.showLoader = false
        this.retryObservable = show
        notifyDataSetChanged()
    }

    fun setIsLastItem(isLast: Boolean){
        this.retryObservable = false
        this.showLoader = false
        this.endObservable = isLast
        Log.e("Zz", "isLast $isLast")
        notifyDataSetChanged()
    }

    private fun hasLoadingFooter(): Boolean {
        return super.getItemCount() != 0 && (showLoader || retryObservable || endObservable)
    }

    interface DeliveryClickListener {
        fun onDeliveryTapped(delivery: DeliveryEntity)
    }
}