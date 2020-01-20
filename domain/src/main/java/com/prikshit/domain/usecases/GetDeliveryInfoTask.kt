package com.prikshit.domain.usecases

import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.qualifiers.Background
import com.prikshit.domain.qualifiers.Foreground
import com.prikshit.domain.repository.DeliveryRepository
import com.prikshit.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

class GetDeliveryInfoTask @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : ObservableUseCase<DeliveryEntity, String>(backgroundScheduler, foregroundScheduler) {

    override fun generateObservable(input: String?): Observable<DeliveryEntity> {
        requireNotNull(input) { "DeliveryID can't be null" }
        return deliveryRepository.getDeliveryInfo(input)
    }

}