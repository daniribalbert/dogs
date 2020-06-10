package com.wes2dogs.data.api

import com.daniribalbert.autodogs.network.BaseResult
import com.daniribalbert.autodogs.network.model.ApiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

suspend fun <T> safeCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): BaseResult<T> {
    return withContext(dispatcher) {
        try {
            BaseResult(apiCall.invoke(), null)
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> BaseResult(null,
                    parseHttpError(throwable)
                )
                is SocketTimeoutException -> BaseResult(
                    null, ApiError.TimeoutError(throwable = throwable)
                )
                else -> BaseResult(null, ApiError.GenericError(throwable, null, null))
            }
        }
    }
}

fun parseHttpError(throwable: HttpException): ApiError = when (throwable.code()) {
    HttpURLConnection.HTTP_UNAUTHORIZED -> ApiError.UnauthorizedError(
        HttpURLConnection.HTTP_UNAUTHORIZED,
        throwable.message()
    )
    HttpURLConnection.HTTP_CLIENT_TIMEOUT,
    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ApiError.TimeoutError(throwable = throwable)
    else -> ApiError.HttpError(throwable.code(), throwable.message())
}
