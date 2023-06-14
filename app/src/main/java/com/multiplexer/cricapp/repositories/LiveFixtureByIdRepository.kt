package com.multiplexer.cricapp.repositories

import android.util.Log
import com.multiplexer.cricapp.models.fixtures.Data
import com.multiplexer.cricapp.services.CricketAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "LiveFixtureByIdReposito"

class LiveFixtureByIdRepository {
    fun getLiveFixtures(
        id: Int, include: String, api_token: String, callback: (Data) -> Unit
    ) {
        CricketAPI.retrofitService.getFixtureById(id, include, api_token)
            .enqueue(object : Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful) {
                        val fixture = response.body()
                        if (fixture != null) {
                            callback(fixture)
                        }
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
    }
}