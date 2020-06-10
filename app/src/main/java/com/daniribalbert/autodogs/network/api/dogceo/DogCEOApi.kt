package com.daniribalbert.autodogs.network.api.dogceo

import com.daniribalbert.autodogs.network.api.dogceo.model.BreedMap
import com.daniribalbert.autodogs.network.api.dogceo.model.DogApiResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface DogCEOApi {
    @GET("breeds/image/random")
    suspend fun getRandomImage(): DogApiResponse<String>

    @GET("breeds/list/all")
    suspend fun getBreedList() : DogApiResponse<BreedMap>

    @GET("https://dog.ceo/api/breed/{breedName}/images/random")
    suspend fun getByBreed(@Path("breedName") breed: String) : DogApiResponse<String>
}