package com.daniribalbert.autodogs.network.dogapi

import com.daniribalbert.autodogs.network.BaseResult
import com.daniribalbert.autodogs.network.model.DogInfo

interface ILoadDogUseCase {

    suspend fun  loadRandomDogImage(): BaseResult<DogInfo>
    suspend fun  loadRandomDogImage(breedName: String): BaseResult<DogInfo>
}