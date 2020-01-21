package com.prikshit.delivery.ui.deliveries.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prikshit.delivery.R
import com.prikshit.delivery.databinding.ItemDeliveryBinding
import com.prikshit.delivery.databinding.ItemLoaderBinding
import com.prikshit.domain.entities.DeliveryEntity
import kotlinx.android.synthetic.main.item_delivery.view.*
import kotlinx.android.synthetic.main.item_end.view.*
import kotlinx.android.synthetic.main.item_loader.view.*

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
        return super.getItemCount() + if (hasFooter()) 1 else 0
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
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_delivery, parent, false
                )
            )
            VIEW_TYPE_END -> return EndVH(
                LayoutInflater.from(parent.context).inflate(R.layout.item_end, parent, false)
            )
            else -> LoadingVH(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_loader, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int
    ) {
        when {
            getItemViewType(position) == VIEW_TYPE_END -> (holder as EndVH).bind()
            getItemViewType(position) == VIEW_TYPE_LOADING -> (holder as LoadingVH).bind(
                showLoader,
                retryObservable
            )
            else -> try {
                (holder as DeliveryVH).bind(getItem(position) as DeliveryEntity)
            } catch (e: Exception) {

            }
        }
    }

    inner class EndVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.endChip.visibility = View.VISIBLE
            itemView.endTV.visibility = View.VISIBLE
            itemView.endChip.setOnClickListener {
                itemView.endChip.visibility = View.GONE
                itemView.endTV.visibility = View.GONE
            }
        }
    }

    inner class LoadingVH(private val binding: ItemLoaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(isLoading: Boolean, retry: Boolean) {
            binding.isLoadingBool = isLoading
            binding.retryBool = retry
            itemView.errorTV.text = retryMsg
            itemView.retry.setOnClickListener {
                retryLive.postValue(true)
            }
        }
    }

    inner class DeliveryVH(private val binding: ItemDeliveryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(delivery: DeliveryEntity?) {
            delivery?.let {
                ViewCompat.setTransitionName(itemView.imageView, it.id)
                ViewCompat.setTransitionName(itemView.fromTV, it.id + "from")
                ViewCompat.setTransitionName(itemView.toTV, it.id + "to")
                ViewCompat.setTransitionName(itemView.textView2, it.id + "from1")
                ViewCompat.setTransitionName(itemView.textView, it.id + "to1")
                binding.delivery = delivery
                Glide.with(itemView.context)
                    .load(delivery.goodsPicture)
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(itemView.context.getDrawable(R.drawable.ic_photo_black_24dp))
                    .apply(RequestOptions().override(80, 80))
                    .into(itemView.imageView)
                itemView.setOnClickListener {
                    listener.onDeliveryTapped(
                        delivery,
                        arrayOf(
                            itemView.imageView,
                            itemView.fromTV,
                            itemView.toTV,
                            itemView.textView2,
                            itemView.textView
                        ),
                        adapterPosition
                    )
                }
            }
        }
    }

    fun showLoading(showLoader: Boolean) {
        this.endObservable = false
        this.showLoader = showLoader
        this.retryObservable = false
        notifyDataSetChanged()
    }

    fun showRetry(show: Boolean, msg: String) {
        this.retryMsg = msg
        this.endObservable = false
        this.showLoader = false
        this.retryObservable = show
        notifyDataSetChanged()
    }

    fun setIsLastItem(isLast: Boolean) {
        this.retryObservable = false
        this.showLoader = false
        this.endObservable = isLast
        notifyDataSetChanged()
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (showLoader || retryObservable || endObservable)
    }

    interface DeliveryClickListener {
        fun onDeliveryTapped(
            delivery: DeliveryEntity,
            arrOfViews: Array<View>,
            adapterPosition: Int
        )
    }
}