package com.prikshit.delivery.ui.deliveries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.prikshit.delivery.BuildConfig
import com.prikshit.delivery.ui.deliveries.paging.DeliveryBoundaryCallback
import com.prikshit.delivery.utils.NetworkUtil
import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.usecases.GetDeliveryFromCacheTask
import com.prikshit.domain.usecases.GetDeliveryListTask
import javax.inject.Inject

class DeliveryViewmodel @Inject constructor(
    deliveryListTask: GetDeliveryListTask,
    getDeliveryFromCacheTask: GetDeliveryFromCacheTask,
    networkUtil: NetworkUtil
) : ViewModel() {

    var deliveryListSource: LiveData<PagedList<DeliveryEntity>>
    var boundaryCallback: DeliveryBoundaryCallback =
        DeliveryBoundaryCallback(deliveryListTask, networkUtil)

    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(BuildConfig.PAGE_SIZE)
            .setPageSize(BuildConfig.PAGE_SIZE)
            .setEnablePlaceholders(true).build()
        deliveryListSource = LivePagedListBuilder(
            getDeliveryFromCacheTask.generateDataList(BuildConfig.PAGE_SIZE), config
        )
            .setBoundaryCallback(boundaryCallback).build()
    }

    override fun onCleared() {
        super.onCleared()
        boundaryCallback.clear()
    }

    fun refreshList() {
        boundaryCallback.onRefresh()
    }

    fun retry(){
        boundaryCallback.retry()
    }
}