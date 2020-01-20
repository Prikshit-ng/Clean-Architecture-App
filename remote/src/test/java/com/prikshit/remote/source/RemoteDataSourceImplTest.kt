package com.prikshit.remote.source

import com.prikshit.data.repository.RemoteDatasource
import com.prikshit.remote.api.DeliveryService
import com.prikshit.remote.mapper.DeliveryNetworkMapper
import com.prikshit.remote.model.DeliveryNetwork
import com.prikshit.remote.source.utils.TestDataGenerator
import io.reactivex.Observable
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class RemoteDataSourceImplTest {

    @Mock
    private lateinit var deliveryService: DeliveryService

    private val deliveryNetworkMapper = DeliveryNetworkMapper()

    private lateinit var remoteDataSource: RemoteDatasource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        remoteDataSource = RemoteDataSourceImpl(
            deliveryNetworkMapper,
            deliveryService
        )
    }

    @Test
    fun testGetDeliveries() {
        val deliveries = TestDataGenerator.generateDeliveryList()
        Mockito.`when`(deliveryService.getDeliveries(0, 20))
            .thenReturn(Observable.just(deliveries))

        remoteDataSource.getDeliveries("0", 20)
            .test()
            .assertSubscribed()
            .assertValue { list ->
                list.containsAll(
                    deliveries.map { deliveryNetworkMapper.from(it) }
                )
            }
            .assertComplete()

        Mockito.verify(deliveryService, Mockito.times(1))
            .getDeliveries(0, 20)
    }

    @Test
    fun testGetDeliveriesError() {
        val errorMsg = "ERROR"
        val limit = 20

        Mockito.`when`(deliveryService.getDeliveries(0, limit))
            .thenReturn(Observable.error(Throwable(errorMsg)))

        remoteDataSource.getDeliveries("0", limit)
            .test()
            .assertSubscribed()
            .assertError {
                it.message == errorMsg
            }
            .assertNotComplete()
    }
}