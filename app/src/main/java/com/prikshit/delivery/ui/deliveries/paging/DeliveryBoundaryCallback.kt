package com.prikshit.delivery.ui.deliveries.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.prikshit.delivery.BuildConfig
import com.prikshit.delivery.utils.NetworkUtil
import com.prikshit.domain.entities.DeliveryEntity
import com.prikshit.domain.usecases.GetDeliveryListTask
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import java.net.UnknownHostException
import javax.inject.Inject

class DeliveryBoundaryCallback @Inject constructor(
    private val getDeliveryListTask: GetDeliveryListTask,
    private val networkUtils: NetworkUtil
) : PagedList.BoundaryCallback<DeliveryEntity>() {

    private var totalCount: Int = 0
    private var isLoaded: Boolean = false
    var status: MutableLiveData<Status> = MutableLiveData()
    private var disposable = CompositeDisposable()


    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        if (!isLoaded) {
            fetchDeiveries(0)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: DeliveryEntity) {
        Log.e("zzz", "Item end " + itemAtEnd.id + " count $totalCount isLoaded $isLoaded")
        super.onItemAtEndLoaded(itemAtEnd)
        if (!isLoaded) {
            isLoaded = true
            fetchDeiveries(totalCount)
        }
    }

    private fun fetchDeiveries(offset: Int) {
        if (!networkUtils.isInternetAvailable()) {
            updateState(Status.NETWORK_ERROR)
            return
        }

        if (offset == 0) {
            updateState(Status.LOADING)
        } else {
            updateState(Status.PAGE_LOADING)
        }

        disposable.add(
            getDeliveryListTask
                .buildUseCase(
                    GetDeliveryListTask.Params(
                        startIndex = offset.toString(),
                        limit = BuildConfig.PAGE_SIZE
                    )
                )
                .map { Resource.success(it) }
                .startWith(Resource.loading())
                .onErrorResumeNext(
                    Function {
                        io.reactivex.Observable.just(Resource.error(it))
                    }
                ).toFlowable(BackpressureStrategy.LATEST)
                .subscribe({ result ->

                    when (result.status) {
                        Status.SUCCESS -> {
                            success(result.data)
                        }
                        Status.ERROR -> {
                            error(result.throwable)
                        }
                        else -> {
                        }
                    }
                }, { e -> error(e) })
        )
    }

    private fun success(list: List<DeliveryEntity>?) {
        if (list == null) {
            isLoaded = true
            updateState(Status.LOADED)
            Log.e("PK", "list == null")
        } else {
            totalCount += list.size
            if (list.size < BuildConfig.PAGE_SIZE) {
                isLoaded = true
                updateState(Status.LOADED)
            } else {
                isLoaded = false
                updateState(Status.DONE)
            }
        }
    }

    private fun error(throwable: Throwable?) {
        throwable?.let {
            if (throwable is UnknownHostException) {
                updateState(Status.NETWORK_ERROR)
            } else {
                updateState(Status.ERROR)
            }
            isLoaded = false
        }
    }

    private fun updateState(state: Status) {
        this.status.postValue(state)
    }

    fun retry() {
        fetchDeiveries(totalCount)
    }

    fun clear() {
        disposable.clear()
    }

    fun onRefresh() {
        totalCount = 0
        isLoaded = false
        disposable.clear()
        onZeroItemsLoaded()
    }
}