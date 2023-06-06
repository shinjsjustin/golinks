package com.example.golinks

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BreedImagesActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_BREED_NAME = "breed_name"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breed_images)

        val breedName = intent.getStringExtra(EXTRA_BREED_NAME) ?: return

        viewManager = GridLayoutManager(this, 3)
        viewAdapter = BreedImageAdapter(listOf())

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DogBreedService::class.java)

        service.getBreedImages(breedName, 15).enqueue(object : Callback<BreedImagesResponse> {
            override fun onResponse(call: Call<BreedImagesResponse>, response: Response<BreedImagesResponse>) {
                if (response.isSuccessful) {
                    val images = response.body()?.message ?: listOf()
                    (recyclerView.adapter as BreedImageAdapter).setData(images)
                } else {
                    Log.e("BreedImagesActivity", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<BreedImagesResponse>, t: Throwable) {
                Log.e("BreedImagesActivity", "Error: $t")
            }
        })
    }
}