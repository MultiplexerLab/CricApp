package com.multiplexer.cricapp.repositories

import android.util.Log
import com.multiplexer.cricapp.models.teamRanking.Data
import com.multiplexer.cricapp.models.teamRanking.TeamWithRank
import com.multiplexer.cricapp.services.CricketAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "FixturesRepository"

class TeamsRepository {
    var cache = listOf<Data>()
    fun getTeamWithRank(
        api_token: String, callback: (List<Data>) -> Unit
    ) {
        if (cache.isNotEmpty()) {
            val list = cache
            getData(api_token, callback)
            callback(list)
        } else {
            getData(api_token, callback)
        }
    }

    private fun getData(
        api_token: String, callback: (List<Data>) -> Unit
    ) {
        CricketAPI.retrofitService.getTeamWithRanking(api_token)
            .enqueue(object : Callback<TeamWithRank> {
                override fun onResponse(
                    call: Call<TeamWithRank>, response: Response<TeamWithRank>
                ) {
                    if (response.isSuccessful) {
                        val fixture = response.body()
                        if (fixture != null) {
                            val dataList = fixture.data
                            cache = dataList
                            callback(dataList)
                        }
                    }
                }

                override fun onFailure(call: Call<TeamWithRank>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
    }
}