package com.daniribalbert.autodogs.network

import com.daniribalbert.autodogs.network.model.ApiError

data class BaseResult<out T>(val result: T?, val error: ApiError? = null)