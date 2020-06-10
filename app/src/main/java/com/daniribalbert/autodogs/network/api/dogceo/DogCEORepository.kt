package com.daniribalbert.autodogs.network.api.dogceo

import com.daniribalbert.autodogs.network.api.dogceo.DogCEOApi
import com.wes2dogs.data.api.safeCall
import kotlinx.coroutines.Dispatchers

class DogCEORepository(private val dogApi: DogCEOApi) {

    suspend fun loadDogImage() =
        safeCall(Dispatchers.IO) { dogApi.getRandomImage() }

}