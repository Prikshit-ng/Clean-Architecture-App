package com.prikshit.domain.usecases

import androidx.paging.DataSource
import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.repository.DeliveryRepository
import com.prikshit.domain.usecases.base.DataListUseCase
import javax.inject.Inject

class GetDeliveryFromCacheTask @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) : DataListUseCase() {
    override fun generateDataList(input: Int): DataSource.Factory<Int, DeliveryEntity> {
        return deliveryRepository.getDeliveryList(input)
    }

}