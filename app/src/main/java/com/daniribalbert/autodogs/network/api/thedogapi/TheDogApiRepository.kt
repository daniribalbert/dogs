package com.daniribalbert.autodogs.network.api.thedogapi

import com.daniribalbert.autodogs.network.BaseResult
import com.wes2dogs.data.api.safeCall
import com.daniribalbert.autodogs.network.api.thedogapi.model.ImageShort
import kotlinx.coroutines.Dispatchers

class TheDogApiRepository(private val dogApi: TheDogApi) {

    suspend fun loadRandomImage(): BaseResult<ImageShort> {
        return safeCall(Dispatchers.IO) { dogApi.getRandomImage().first() }
    }

    suspend fun loadByBreed(breed: String): BaseResult<ImageShort> {
        return safeCall(Dispatchers.IO) { dogApi.getByBreed(breed).first() }
    }
}