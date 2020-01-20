package com.prikshit.domain.usecases.base

import androidx.paging.DataSource
import com.prikshit.domain.entities.DeliveryEntity

abstract class DataListUseCase {
    abstract fun generateDataList(input: Int): DataSource.Factory<Int, DeliveryEntity>
}