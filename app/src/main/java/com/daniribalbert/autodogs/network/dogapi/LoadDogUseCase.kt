package com.daniribalbert.autodogs.network.dogapi

import com.daniribalbert.autodogs.network.BaseResult
import com.daniribalbert.autodogs.network.api.dogceo.DogCEORepository
import com.daniribalbert.autodogs.network.api.thedogapi.TheDogApiRepository
import com.daniribalbert.autodogs.network.model.ApiError
import com.daniribalbert.autodogs.network.model.DogInfo

class LoadDogUseCase(
    private val theDogApiRepository: TheDogApiRepository,
    private val dogCEORepository: DogCEORepository
): ILoadDogUseCase {

    override suspend fun loadRandomDogImage(): BaseResult<DogInfo> {
        val dogApiResult = theDogApiRepository.loadRandomImage()
        dogApiResult.result?.let {
            val name = it.breeds.firstOrNull()?.name ?: ""
            val url = it.url
            val temperament = it.breeds.firstOrNull()?.temperament ?: ""
            val origin = it.breeds.firstOrNull()?.origin ?: ""

            return BaseResult(
                DogInfo(
                    name,
                    url,
                    temperament,
                    origin
                )
            )
        }
        val dogCEOResult = dogCEORepository.loadDogImage()
        dogCEOResult.result?.let {
            return BaseResult(DogInfo(it.message))
        }

        dogApiResult.error?.let {
            return BaseResult(null, it)
        }

        dogCEOResult.error?.let {
            return BaseResult(null, it)
        }

        return BaseResult(
            null,
            ApiError.GenericError(Throwable(), -1, "")
        )
    }

    override suspend fun loadRandomDogImage(breedName: String): BaseResult<DogInfo> {
        TODO("Not yet implemented")
    }

}
