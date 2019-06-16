package com.synchroniverse.bowwow.services

import com.synchroniverse.bowwow.models.ImageApiModel
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breeds/list/all")
    fun getBreedListAsync() : Call<ResponseBody>

    @GET("breed/{breed}/images")
    fun getBreedImagesAsync(@Path("breed") breed: String) : Call<ImageApiModel>

    @GET("breed/{breed}/{subBreed}/images")
    fun getSubBreedImagesAsync(@Path("breed") breed: String, @Path("subBreed") subBreed : String) : Call<ImageApiModel>
}