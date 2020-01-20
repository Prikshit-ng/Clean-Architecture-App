package com.prikshit.delivery.ui.deliveries.paging

data class Resource<out T>(val status: Status, val data: T?, val throwable: Throwable?) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(msg: Throwable, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                msg
            )
        }

        fun <T> loading(): Resource<T> {
            return Resource(
                Status.LOADING,
                null,
                null
            )
        }
    }
}

enum class Status {
    LOADING,
    ERROR,
    SUCCESS,
    NETWORK_ERROR,
    PAGE_LOADING,
    LOADED,
    DONE
}