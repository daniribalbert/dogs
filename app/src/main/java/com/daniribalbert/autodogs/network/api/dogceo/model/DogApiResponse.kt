package com.daniribalbert.autodogs.network.api.dogceo.model

data class DogApiResponse<T>(val message: T, val status: String)