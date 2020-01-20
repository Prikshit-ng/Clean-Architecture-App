package com.prikshit.data.repository

import com.prikshit.data.mapper.DeliveryDomainMapper
import com.prikshit.data.repository.utils.TestDataGenerator
import com.prikshit.domain.repository.DeliveryRepository
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DeliveryRepositoryImplTest {

    private lateinit var deliveryRepository: DeliveryRepository

    private val deliveryDomainMapper = DeliveryDomainMapper()

    @Mock
    private lateinit var localDataSource: LocalDataSource
    @Mock
    private lateinit var remoteDataSource: RemoteDatasource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        deliveryRepository = DeliveryRepositoryImpl(
            deliveryDomainMapper,
            localDataSource,
            remoteDataSource
        )
    }
    @Test
    fun testGetDeliveryInfo() {
        val deliveryInfoData = TestDataGenerator.generateDeliveryList()
        val deliveryInfoDomain = deliveryDomainMapper.from(deliveryInfoData[0])

        Mockito.`when`(remoteDataSource.getDeliveries("0", 20))
            .thenReturn(Observable.just(deliveryInfoData))
        Mockito.`when`(localDataSource.getDeliveryById(deliveryInfoDomain.id))
            .thenReturn(Observable.just(deliveryInfoData[0]))

        val testSubscriber = deliveryRepository.getDeliveryInfo(deliveryInfoDomain.id).test()

        testSubscriber.assertSubscribed()
            .assertValueCount(1)
            .assertValues(deliveryInfoDomain)
            .assertComplete()

        Mockito.verify(localDataSource, Mockito.times(0))
            .saveDeliveries(deliveryInfoData)

        Mockito.verify(remoteDataSource, Mockito.times(0))
            .getDeliveries("0", 20)
    }

    @Test
    fun testGetDeliveryInfoError() {
        val deliveryInfoData = TestDataGenerator.generateDeliveryList()
        val deliveryInfoDomain = deliveryDomainMapper.from(deliveryInfoData[0])

        Mockito.`when`(remoteDataSource.getDeliveries("0", 20))
            .thenReturn(Observable.just(deliveryInfoData))
        Mockito.`when`(localDataSource.getDeliveryById(deliveryInfoDomain.id))
            .thenReturn(Observable.just(deliveryInfoData[0]))

        val testSubscriber = deliveryRepository.getDeliveryInfo(deliveryInfoDomain.id).test()

        testSubscriber.assertSubscribed()
            .assertValueCount(1)
            .assertValue {
                it == deliveryInfoDomain
            }
            .assertComplete()

        Mockito.verify(localDataSource, Mockito.times(0))
            .getDeliveries()
    }

    @Test
    fun testUpdateDelivery() {
        val deliveryData = TestDataGenerator.generateDeliveryList()[0]
        val deliveryEntity = deliveryDomainMapper.from(deliveryData)

        Mockito.`when`(localDataSource.updateDelivery(deliveryData))
            .thenReturn(Completable.complete())

        val testObserver = deliveryRepository.updateDelivery(deliveryEntity).test()

        testObserver.assertSubscribed()

        Mockito.verify(localDataSource, Mockito.times(1))
            .updateDelivery(deliveryData)
    }
}