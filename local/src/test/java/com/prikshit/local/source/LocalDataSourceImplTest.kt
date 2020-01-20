package com.prikshit.local.source

import com.prikshit.data.repository.LocalDataSource
import com.prikshit.local.database.DeliveryDB
import com.prikshit.local.database.DeliveryDao
import com.prikshit.local.mapper.DeliveryDataMapper
import com.prikshit.local.source.utils.TestDataGenerator
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class LocalDataSourceImplTest {

    private val deliveryDataMapper = DeliveryDataMapper()

    @Mock
    private lateinit var deliveryDao: DeliveryDao

    @Mock lateinit var deliveryDB: DeliveryDB

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        localDataSource = LocalDataSourceImpl(
            deliveryDB,
            deliveryDataMapper,
            deliveryDao
        )
    }

    @Test
    fun testGetDeliveryById() {
        val mockDelivery = TestDataGenerator.generateDeliveryList()[0]
        val deliveryId = mockDelivery.id

        Mockito.`when`(deliveryDao.getDeliveryById(deliveryId))
            .thenReturn(Observable.just(mockDelivery))

        localDataSource.getDeliveryById(deliveryId)
            .test()
            .assertSubscribed()
            .assertValue {
                mockDelivery == deliveryDataMapper.to(it)
            }
    }

    @Test
    fun testGetDeliveryByIdError() {
        val mockDeliveries = TestDataGenerator.generateDeliveryList()

        Mockito.`when`(deliveryDao.getDeliveryById( mockDeliveries[0].id))
            .thenReturn(Observable.just(mockDeliveries[0]))
    }

    @Test
    fun saveDeliveries() {
        val mockDeliveries = TestDataGenerator.generateDeliveryList()

        localDataSource.saveDeliveries(
            mockDeliveries.map {
                deliveryDataMapper.from(it)
            }
        )

        Mockito.verify(deliveryDao, Mockito.times(1))
            .saveDeliveries(mockDeliveries)
    }

    @Test
    fun updateDelivery() {
        val deliveryLocal = TestDataGenerator.generateDeliveryList()[0]
        val deliveryData = deliveryDataMapper.from(deliveryLocal)

        Mockito.`when`(deliveryDao.updateDelivery(deliveryLocal))
            .thenReturn(Completable.complete())

        localDataSource.updateDelivery(deliveryData)
            .test()
            .assertSubscribed()
            .assertComplete()

        Mockito.verify(deliveryDao, Mockito.times(1))
            .updateDelivery(deliveryLocal)
    }
}