package com.prikshit.remote.source

import com.prikshit.data.repository.RemoteDatasource
import com.prikshit.remote.api.DeliveryService
import com.prikshit.remote.mapper.DeliveryNetworkMapper
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

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