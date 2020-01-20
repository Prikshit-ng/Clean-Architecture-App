package com.prikshit.domain.usecases

import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.qualifiers.Background
import com.prikshit.domain.qualifiers.Foreground
import com.prikshit.domain.repository.DeliveryRepository
import com.prikshit.domain.usecases.base.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.Scheduler
import javax.inject.Inject

class UpdateDeliveryTask @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : CompletableUseCase<DeliveryEntity>(backgroundScheduler, foregroundScheduler) {

    override fun generateCompletable(input: DeliveryEntity?): Completable {
        requireNotNull(input) { "Delivery Entity can't be null" }
        return deliveryRepository.updateDelivery(input)
    }

}