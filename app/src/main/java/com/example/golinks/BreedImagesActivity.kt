package com.example.golinks

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
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

        //Progress Bar init
        val pBar = findViewById<ProgressBar>(R.id.progressBar)
        pBar.visibility = View.VISIBLE

        //Breed Name init
        val breedName = intent.getStringExtra(EXTRA_BREED_NAME) ?: return

        //Root Layout color change
        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
        when(breedName){
            "whippet" -> rootLayout.setBackgroundColor(Color.parseColor("#FFC0CB"))
            "greyhound/italian" -> rootLayout.setBackgroundColor(Color.parseColor("#800080"))
        }

        //Manager and Adapter init
        viewManager = GridLayoutManager(this, 3)
        viewAdapter = BreedImageAdapter(listOf())

        //Change Title
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = breedName.replace("/", " ")

        //implement adapter and manager
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //Data fetching init
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(DogBreedService::class.java)
        //-Fetch breed or sub-breed
        val (breed, subBreed) = breedName.split('/').let {
            if(it.size==2) Pair(it[0], it[1]) else Pair(it[0],"")
        }
        val call = if(subBreed.isNotEmpty()){
            service.getSubBreedImages(breed, subBreed, 15)
        }else{
            service.getBreedImages(breed, 15)
        }
        //-Fetching
        call.enqueue(object : Callback<BreedImagesResponse> {
            override fun onResponse(call: Call<BreedImagesResponse>, response: Response<BreedImagesResponse>) {
                pBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val images = response.body()?.message ?: listOf()
                    (recyclerView.adapter as BreedImageAdapter).setData(images)
                } else {
                    Log.e("BreedImagesActivity", "Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<BreedImagesResponse>, t: Throwable) {
                pBar.visibility = View.GONE
                Log.e("BreedImagesActivity", "Error: $t")
            }
        })
    }
}