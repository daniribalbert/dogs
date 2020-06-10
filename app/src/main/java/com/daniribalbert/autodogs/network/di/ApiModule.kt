package com.daniribalbert.autodogs.network.di

import com.daniribalbert.autodogs.network.api.dogceo.DogCEOApi
import com.daniribalbert.autodogs.network.api.dogceo.DogCEORepository
import com.daniribalbert.autodogs.network.api.thedogapi.TheDogApi
import com.daniribalbert.autodogs.network.api.thedogapi.TheDogApiRepository
import com.daniribalbert.autodogs.network.dogapi.ILoadDogUseCase
import com.daniribalbert.autodogs.network.dogapi.LoadDogUseCase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideAuthInterceptor() = object: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().apply {
            addHeader("x-api-key", API_KEY)
        }.build()
        return chain.proceed(newRequest)
    }

}

fun provideOkHttp(authInterceptor: Interceptor?) = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .also { builder  -> authInterceptor?.let { builder.addInterceptor(it) } }
    .build()

fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()

fun provideDogCEOApi(retrofit: Retrofit) = retrofit.create(DogCEOApi::class.java)
fun provideTheDogApi(retrofit: Retrofit) = retrofit.create(TheDogApi::class.java)

val apiModule = module {

    single<Interceptor?>(named(NAME_THE_DOG_API)) { provideAuthInterceptor() }

    single(named(NAME_THE_DOG_API)) { provideOkHttp(get(named(NAME_THE_DOG_API))) }
    single(named(NAME_DOG_CEO)) { provideOkHttp(null) }

    single(named(NAME_DOG_CEO)) { provideRetrofit(get(named(NAME_DOG_CEO)), DOG_CEO_API_BASE_URL) }
    single(named(NAME_THE_DOG_API)) { provideRetrofit(get(named(NAME_THE_DOG_API)), THE_DOG_API_BASE_URL) }

    single { provideDogCEOApi(get(named(NAME_DOG_CEO))) }
    single { provideTheDogApi(get(named(NAME_THE_DOG_API))) }

    single { TheDogApiRepository(get()) }
    single { DogCEORepository(get()) }

    single<ILoadDogUseCase> { LoadDogUseCase(get(), get()) }

}

private const val NAME_DOG_CEO = "dog-ceo"
private const val NAME_THE_DOG_API = "the-dog-api"

private const val DOG_CEO_API_BASE_URL = "https://dog.ceo/api/"
private const val THE_DOG_API_BASE_URL = "https://api.thedogapi.com/"
private const val API_KEY = "2cdb79b8-f5d3-4686-936a-cdb11a467d91"