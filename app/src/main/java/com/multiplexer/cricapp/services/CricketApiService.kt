package com.multiplexer.cricapp.services

import com.multiplexer.cricapp.models.continents.Continent
import com.multiplexer.cricapp.models.countries.Country
import com.multiplexer.cricapp.models.fixtures.Data
import com.multiplexer.cricapp.models.fixtures.Fixture
import com.multiplexer.cricapp.models.leagues.League
import com.multiplexer.cricapp.models.palyers.Player
import com.multiplexer.cricapp.models.playersList.Players
import com.multiplexer.cricapp.models.teamRanking.TeamWithRank
import com.multiplexer.cricapp.models.teams.Team
import com.multiplexer.cricapp.utlis.Constants
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val timeout = 20000 // 10 seconds
val client: OkHttpClient =
    OkHttpClient.Builder().connectTimeout(timeout.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(timeout.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(timeout.toLong(), TimeUnit.MILLISECONDS).build()

val retrofit: Retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
    .baseUrl(Constants.BASE_URL).client(client).build()

interface CricketApiService {
    @GET(Constants.countries)
    suspend fun getAllCountries(
        @Query(Constants.api_token) api_token: String
    ): Country

    @GET(Constants.continents)
    suspend fun getAllContinents(
        @Query(Constants.api_token) api_token: String
    ): Continent

    @GET(Constants.teams)
    suspend fun getAllTeams(
        @Query(Constants.api_token) api_token: String
    ): Team

    @GET(Constants.league)
    suspend fun getAllLeague(
        @Query(Constants.api_token) api_token: String
    ): League

    @GET(Constants.playerById)
    fun getPlayerDetails(
        @Path(Constants.playerId) playerId: Int,
        @Query(Constants.include) include: String,
        @Query(Constants.api_token) api_token: String,
    ): Call<Player>

    @GET(Constants.players)
    fun getPlayersByPosition(
        @Query(Constants.filterByPositionID) positionId: Int,
        @Query(Constants.filterByFields) fields: String,
        @Query(Constants.api_token) api_token: String,
    ): Call<Players>

    @GET(Constants.teamRanking)
    fun getTeamWithRanking(
        @Query(Constants.api_token) api_token: String
    ): Call<TeamWithRank>

    @GET(Constants.fixtures)
    fun getFixtures(
        @Query(Constants.filterDate) startsBetween: String,
        @Query(Constants.include) include: String,
        @Query(Constants.filterStatus) status: String,
        @Query(Constants.sort) sort: String,
        @Query(Constants.api_token) api_token: String,
    ): Call<Fixture>

    @GET(Constants.liveFixtures)
    fun getLiveFixtures(
        @Query(Constants.include) include: String,
        @Query(Constants.api_token) api_token: String,
    ): Call<Fixture>


    @GET(Constants.fixtures)
    fun getFixtureByLeagueId(
        @Query(Constants.filterByLeagueId) id: Int,
        @Query(Constants.filterDate) date: String,
        @Query(Constants.include) include: String,
        @Query(Constants.sort) sort: String,
        @Query(Constants.api_token) api_token: String,
    ): Call<Fixture>

    @GET(Constants.fixtureById)
    fun getFixtureById(
        @Path(Constants.fixtureId) id: Int,
        @Query(Constants.include) include: String,
        @Query(Constants.api_token) api_token: String,
    ): Call<Data>

    @GET(Constants.fixtures)
    fun getFixtureByTeamId(
        @Query(Constants.filterByTeamId) id: Int,
        @Query(Constants.filterDate) date: String,
        @Query(Constants.include) include: String,
        @Query(Constants.sort) sort: String,
        @Query(Constants.api_token) api_token: String,
    ): Call<Fixture>
}

object CricketAPI {
    val retrofitService: CricketApiService by lazy { retrofit.create(CricketApiService::class.java) }
}