package com.example.golinks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.golinks.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

const val BASE_URL = "https://dog.ceo/"
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Manager and Adapter init
        viewManager = LinearLayoutManager(this)
        viewAdapter = DogBreedAdapter(listOf()){breedName->
            val intent = Intent(this, BreedImagesActivity::class.java).apply{
                putExtra(BreedImagesActivity.EXTRA_BREED_NAME, breedName)
            }
            startActivity(intent)
        }
        //Manager and Adapter implementation
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //Data Fetching init
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DogBreedService::class.java)
        //-Data Fetch Call
        service.listBreeds().enqueue(object : Callback<DogBreedResponse> {
            override fun onResponse(call: Call<DogBreedResponse>, response: Response<DogBreedResponse>) {
                if (response.isSuccessful) {
                    val dogBreedMap = response.body()?.message?: emptyMap()
                    val breedList = mutableListOf<Pair<String, String>>()
                    for ((breed, subBreeds) in dogBreedMap) {
                        if (subBreeds.isEmpty()) {
                            breedList.add(Pair(breed, ""))
                        } else {
                            for (subBreed in subBreeds) {
                                breedList.add(Pair(breed, subBreed))
                            }
                        }
                    }
                    (recyclerView.adapter as DogBreedAdapter).setData(breedList)
                } else {
                    Log.e("MainActivity", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<DogBreedResponse>, t: Throwable) {
                Log.e("MainActivity", "Error: $t")
            }
        })
    }
}