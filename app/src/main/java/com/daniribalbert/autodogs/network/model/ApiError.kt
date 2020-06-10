package com.daniribalbert.autodogs.network.model

sealed class ApiError(val errorMsg: String?) {
    class HttpError(val errorCode: Int, errorMsg: String?): ApiError(errorMsg)
    class UnauthorizedError(val errorCode: Int = 401, errorMsg: String?): ApiError(errorMsg)
    class TimeoutError(errorMsg: String = "Timeout", val throwable: Throwable): ApiError(errorMsg)
    class GenericError(throwable: Throwable, val errorCode: Int?, errorMsg: String?): ApiError(errorMsg)
    class UnknownError(): ApiError("")
}