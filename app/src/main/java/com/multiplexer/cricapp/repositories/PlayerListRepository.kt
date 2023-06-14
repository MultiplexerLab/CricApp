package com.multiplexer.cricapp.repositories

import android.util.Log
import com.multiplexer.cricapp.models.playersList.Data
import com.multiplexer.cricapp.models.playersList.Players
import com.multiplexer.cricapp.services.CricketAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "PlayerListRepository"

class PlayerListRepository {
    var cache = listOf<Data>()
    fun getPlayerInfo(
        positionId: Int, fields: String, api_token: String, callback: (List<Data>) -> Unit
    ) {
        CricketAPI.retrofitService.getPlayersByPosition(positionId, fields, api_token)
            .enqueue(object : Callback<Players> {
                override fun onResponse(call: Call<Players>, response: Response<Players>) {
                    if (response.isSuccessful) {
                        val fixture = response.body()
                        if (fixture != null) {
                            val dataList = fixture.data
                            cache = dataList
                            callback(dataList)
                        }
                    }
                }

                override fun onFailure(call: Call<Players>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
    }
}