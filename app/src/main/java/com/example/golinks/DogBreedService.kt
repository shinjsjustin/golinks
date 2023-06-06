package com.example.golinks

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogBreedService {
    @GET(value="api/breeds/list/all")
    fun listBreeds(): Call<DogBreedResponse>

    @GET("api/breed/{breed}/images/random/{count}")
    fun getBreedImages(@Path("breed") breed: String, @Path("count")count: Int): Call<BreedImagesResponse>
}