package com.prikshit.domain.usecases

import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.qualifiers.Background
import com.prikshit.domain.qualifiers.Foreground
import com.prikshit.domain.repository.DeliveryRepository
import com.prikshit.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

class GetDeliveryListTask @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : ObservableUseCase<List<DeliveryEntity>, GetDeliveryListTask.Params>(
    backgroundScheduler,
    foregroundScheduler
) {
    override fun generateObservable(input: Params?): Observable<List<DeliveryEntity>> {
        requireNotNull(input) { "GetDeliveryListTask parameter can't be null" }
        return deliveryRepository.getDeliveryList(input.startIndex, input.limit)
    }

    data class Params(val startIndex: String, val limit: Int)
}