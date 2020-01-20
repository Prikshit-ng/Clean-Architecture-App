package com.prikshit.delivery.ui.deliveryDetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.usecases.GetDeliveryInfoTask
import com.prikshit.domain.usecases.UpdateDeliveryTask
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import javax.inject.Inject

class DeliveryDetailViewmodel @Inject internal constructor(
    private val getDeliveryInfoTask: GetDeliveryInfoTask,
    private val updateDeliveryTask: UpdateDeliveryTask
) : ViewModel() {

    fun detDeliveryById(deliveryID: String): LiveData<DeliveryEntity> {
        return getDeliveryInfoTask.buildUseCase(deliveryID).toFlowable(BackpressureStrategy.LATEST)
            .toLiveData()
    }

    fun updateDelivery(delivery: DeliveryEntity): Completable {
        return updateDeliveryTask.buildUseCase(delivery)
    }
}