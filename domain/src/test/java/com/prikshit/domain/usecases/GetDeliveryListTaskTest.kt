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
class GetDeliveryListTaskTest {

    private lateinit var getDeliveryListTask: GetDeliveryListTask

    @Mock
    private lateinit var deliveryRepository: DeliveryRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getDeliveryListTask = GetDeliveryListTask(
            deliveryRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun test_deliveryListTask_success() {
        val deliveryList = TestDataGenerator.generateDeliveryList()
        val startIndex ="0"
        val limit =20

        Mockito.`when`(deliveryRepository.getDeliveryList(startIndex,limit))
            .thenReturn(Observable.just(deliveryList))

        val testObserver = getDeliveryListTask.buildUseCase(
            GetDeliveryListTask.Params(startIndex, limit)
        ).test()


        testObserver
            .assertSubscribed()
            .assertValue { it.containsAll(deliveryList) }
    }

    @Test
    fun test_deliveryListTask_error() {
        val startIndex ="0"
        val limit =20
        val errorMsg = "ERROR OCCURRED"
        Mockito.`when`(deliveryRepository.getDeliveryList(startIndex,limit))
            .thenReturn(Observable.error(Throwable(errorMsg)))

        val testObserver = getDeliveryListTask.buildUseCase(
            GetDeliveryListTask.Params(startIndex, limit)
        ).test()

        testObserver
            .assertSubscribed()
            .assertError { it.message?.equals(errorMsg, false) ?: false }
            .assertNotComplete()
    }

    @Test(expected = IllegalArgumentException::class)
    fun test_deliveryListTaskNoParameters_error() {
        val testObserver = getDeliveryListTask.buildUseCase().test()
        testObserver.assertSubscribed()
    }
}