package com.example.golinks

import retrofit2.Call
import retrofit2.http.GET

interface DogBreedService {
    @GET(value="api/breeds/list/all")
    fun listBreeds(): Call<DogBreedResponse>
}