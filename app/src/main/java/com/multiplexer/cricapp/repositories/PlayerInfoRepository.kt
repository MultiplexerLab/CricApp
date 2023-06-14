package com.multiplexer.cricapp.repositories

import android.util.Log
import com.multiplexer.cricapp.models.palyers.Data
import com.multiplexer.cricapp.models.palyers.Player
import com.multiplexer.cricapp.services.CricketAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "PlayerInfoRepository"

class PlayerInfoRepository {
    var cache: Data? = null
    fun getPlayerInfo(
        playerId: Int, include: String, api_token: String, callback: (Data) -> Unit
    ) {
        if (cache != null) {
            val list = cache
            if (list != null) {
                callback(list)
            }
        } else {
            getData(playerId, include, api_token, callback)
        }
    }

    private fun getData(
        playerId: Int, include: String, api_token: String, callback: (Data) -> Unit
    ) {
        CricketAPI.retrofitService.getPlayerDetails(playerId, include, api_token)
            .enqueue(object : Callback<Player> {
                override fun onResponse(call: Call<Player>, response: Response<Player>) {
                    if (response.isSuccessful) {
                        val player = response.body()
                        if (player != null) {
                            val dataList = player.data
                            cache = dataList
                            if (dataList != null) {
                                callback(dataList)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Player>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
    }
}