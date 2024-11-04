package com.misw.appvynills.utils

inline fun <T> resultOrError(block: () -> T): DataState<T> {
    return try {
        DataState.Success(block())
    } catch (e: Exception) {
        DataState.Error(e)
    }
}