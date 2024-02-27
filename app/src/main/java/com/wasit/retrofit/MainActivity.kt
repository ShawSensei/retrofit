package com.wasit.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.wasit.retrofit.Constants.BASE_URL
import com.wasit.retrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create





class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userAdapter: UserAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerviewUsers.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerviewUsers.layoutManager= linearLayoutManager

        getUserData();
    }

    private fun getUserData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<UserDataItem>?> {
            override fun onResponse(
                call: Call<List<UserDataItem>?>,
                response: Response<List<UserDataItem>?>
            ) {
                val responseBody = response.body()!!
                Log.d("MainActivity", "$responseBody")

               userAdapter= UserAdapter(baseContext, responseBody)
                userAdapter.notifyDataSetChanged()
                binding.recyclerviewUsers.adapter = userAdapter
            }

            override fun onFailure(call: Call<List<UserDataItem>?>, t: Throwable) {
                Log.d("MainActivity","onFailure"+t.message)

            }
        })

        }
    }
