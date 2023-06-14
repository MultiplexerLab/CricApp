package com.multiplexer.cricapp.repositories

import android.util.Log
import com.multiplexer.cricapp.models.fixtures.Data
import com.multiplexer.cricapp.models.fixtures.Fixture
import com.multiplexer.cricapp.services.CricketAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "FixturesRepository"

class FixturesRepository {
    var flag = false
    val cache = HashMap<String, List<Data>>()
    fun getFixtures(
        startsBetween: String,
        include: String,
        status: String,
        sort: String,
        api_token: String,
        callback: (List<Data>) -> Unit
    ) {
        Log.d(TAG, "$startsBetween ")
        if (cache.containsKey(startsBetween)) {
            val list = cache[startsBetween]
            getData(startsBetween, include, status, sort, api_token, callback)

            cache[startsBetween]?.map {
                if (list != null) {
                    Log.d(TAG, "getFixtures: ")
                    flag = !list.contains(it)
                }
            }
            if (flag) {
                Log.d(TAG, "$flag")
                callback(cache[startsBetween]!!)
            } else {
                callback(list!!)
            }
        } else {
            getData(startsBetween, include, status, sort, api_token, callback)
        }
    }

    private fun getData(
        startsBetween: String,
        include: String,
        status: String,
        sort: String,
        api_token: String,
        callback: (List<Data>) -> Unit
    ) {
        CricketAPI.retrofitService.getFixtures(startsBetween, include, status, sort, api_token)
            .enqueue(object : Callback<Fixture> {
                override fun onResponse(call: Call<Fixture>, response: Response<Fixture>) {
                    if (response.isSuccessful) {
                        val fixture = response.body()
                        if (fixture != null) {
                            val dataList = fixture.data
                            cache[startsBetween] = dataList
                            callback(dataList)
                        }
                    }
                }

                override fun onFailure(call: Call<Fixture>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
    }
}