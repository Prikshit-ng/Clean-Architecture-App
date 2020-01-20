package com.prikshit.domain.usecases

import com.prikshit.domain.repository.DeliveryRepository
import com.prikshit.domain.util.TestDataGenerator
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.IllegalArgumentException

@RunWith(JUnit4::class)
class GetDeliveryInfoTaskTest {

    private lateinit var getDeliveryInfoTask: GetDeliveryInfoTask

    @Mock
    private lateinit var deliveryRepository: DeliveryRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getDeliveryInfoTask = GetDeliveryInfoTask(
            deliveryRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun test_deliveryInfoTask_success() {
        val delivery = TestDataGenerator.generateDeliveryList()[1]
        val id = delivery.id

        Mockito.`when`(deliveryRepository.getDeliveryInfo(id))
            .thenReturn(Observable.just(delivery))

        val testObserver = getDeliveryInfoTask.buildUseCase(
            id
        ).test()


        testObserver
            .assertSubscribed()
            .assertValue { it == delivery }
            .assertComplete()
    }

    @Test
    fun test_deliveryInfoTask_error() {
        val delivery = TestDataGenerator.generateDeliveryList()[1]
        val errorMsg = "ERROR OCCURRED"
        val id = delivery.id

        Mockito.`when`(deliveryRepository.getDeliveryInfo(id))
            .thenReturn(Observable.error(Throwable(errorMsg)))

        val testObserver = getDeliveryInfoTask.buildUseCase(
            id
        ).test()


        testObserver
            .assertSubscribed()
            .assertError { it.message?.equals(errorMsg, false) ?: false }
            .assertNotComplete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun test_deliveryInfoTaskNoParameters_error() {
        val testObserver = getDeliveryInfoTask.buildUseCase().test()
        testObserver.assertSubscribed()
    }
}