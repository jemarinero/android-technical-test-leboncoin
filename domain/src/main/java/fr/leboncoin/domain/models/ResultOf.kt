package fr.leboncoin.domain.models

sealed class ResultOf<out T> {
    data class Success<out T>(val value: T): ResultOf<T>()
    data class Failure(val errorType: ErrorType): ResultOf<Nothing>()
}

inline fun <reified T> ResultOf<T>.doIfFailure(callback: (failure: ErrorType?) -> Unit) {
    if (this is ResultOf.Failure) {
        callback(errorType)
    }
}

inline fun <reified T> ResultOf<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ResultOf.Success) {
        callback(value)
    }
}