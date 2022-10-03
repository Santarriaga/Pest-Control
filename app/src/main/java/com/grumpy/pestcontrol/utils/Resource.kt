package com.grumpy.pestcontrol.utils

/**
 * Helps manage our state for ui
 * changed name from state to resource
 */

sealed class Resource<out T> {
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Failed<T>(val message: String) : Resource<T>()

    companion object{
        fun <T> loading() = Loading<T>()
        fun <T> success(data : T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }

}