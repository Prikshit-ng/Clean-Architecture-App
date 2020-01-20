package com.prikshit.delivery.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
open class ViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var provider = providers[modelClass]
        if (provider == null) {
            for ((key, value) in providers) {
                if (modelClass.isAssignableFrom(key)) {
                    provider = value
                    break
                }
            }
        }

        checkNotNull(provider) { "Unknown View Model $modelClass" }

        return try {
            provider.get() as T
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }
}