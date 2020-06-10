package com.daniribalbert.autodogs.network.api.thedogapi


import com.daniribalbert.autodogs.network.api.thedogapi.model.Breed
import com.daniribalbert.autodogs.network.api.thedogapi.model.ImageShort
import retrofit2.http.GET
import retrofit2.http.Query


interface TheDogApi {
    @GET("v1/images/search")
    suspend fun getRandomImage(): List<ImageShort>

    @GET("v1/breeds")
    suspend fun getBreedList() : List<Breed>

    @GET("v1/images/search")
    suspend fun getByBreed(@Query("breed_ids") id: String) : List<ImageShort>
}