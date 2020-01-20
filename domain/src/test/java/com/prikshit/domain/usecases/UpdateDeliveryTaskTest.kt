package com.prikshit.domain.usecases

import com.prikshit.domain.repository.DeliveryRepository
import com.prikshit.domain.util.TestDataGenerator
import io.reactivex.Completable
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
class UpdateDeliveryTaskTest {

    private lateinit var updateDeliveryTask: UpdateDeliveryTask

    @Mock
    private lateinit var deliveryRepository: DeliveryRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        updateDeliveryTask = UpdateDeliveryTask(
            deliveryRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun test_deliveryUpdaterTask_success() {
        val delivery = TestDataGenerator.generateDeliveryList()[0]

        Mockito.`when`(deliveryRepository.updateDelivery(delivery))
            .thenReturn(Completable.complete())

        val testObserver = updateDeliveryTask.buildUseCase(
            delivery
        ).test()

        Mockito.verify(deliveryRepository, Mockito.times(1))
            .updateDelivery(delivery)

        testObserver
            .assertSubscribed()
            .assertComplete()
    }

    @Test
    fun test_deliveryUpdaterTask_error() {
        val delivery = TestDataGenerator.generateDeliveryList()[0]
        val errorMsg = "ERROR OCCURRED"

        Mockito.`when`(deliveryRepository.updateDelivery(delivery))
            .thenReturn(Completable.error(Throwable(errorMsg)))

        val testObserver = updateDeliveryTask
            .buildUseCase(delivery).test()

        Mockito.verify(deliveryRepository, Mockito.times(1))
            .updateDelivery(delivery)

        testObserver
            .assertSubscribed()
            .assertError { it.message?.equals(errorMsg) ?: false }
    }

    @Test(expected = IllegalArgumentException::class)
    fun test_deliveryUpdaterTaskNoParameters_error() {
        val testObserver = updateDeliveryTask.buildUseCase().test()
        testObserver.assertSubscribed()
    }
}