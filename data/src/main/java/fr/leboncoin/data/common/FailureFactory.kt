package fr.leboncoin.data.common

import fr.leboncoin.data.network.model.HttpCode
import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.domain.models.ResultOf
import okhttp3.ResponseBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class FailureFactory {
    open fun handleCode(code: Int, errorBody: ResponseBody?) =
        ResultOf.Failure(errorType = when (HttpCode.fromCode(code)) {
            HttpCode.NOT_FOUND,
            HttpCode.BAD_REQUEST -> ErrorType.UnknownError
            HttpCode.UNAUTHORIZED,
            HttpCode.FORBIDDEN -> ErrorType.ServerError
            HttpCode.TIME_OUT,
            HttpCode.SERVER_ERROR ->  ErrorType.ServerError
            else -> ErrorType.UnknownError
        })

    open fun handleException(exception: Throwable) =
        ResultOf.Failure(errorType = when (exception) {
            is UnknownHostException, is SocketTimeoutException -> ErrorType.ConnectivityError
            else -> ErrorType.UnknownError
        })
}