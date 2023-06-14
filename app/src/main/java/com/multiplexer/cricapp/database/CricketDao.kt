package com.multiplexer.cricapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CricketDao {

    @Query("SELECT * FROM countries order by name")
    fun getAllCountries(): LiveData<List<com.multiplexer.cricapp.models.countries.Data>>

    @Query("SELECT * FROM teams group by country_id")
    fun getAllCountriesFromTeams(): LiveData<List<com.multiplexer.cricapp.models.countries.Data>>

    @Query("SELECT * FROM continents")
    fun getAllContinents(): LiveData<List<com.multiplexer.cricapp.models.continents.Data>>

    @Query("SELECT * FROM teams")
    fun getAllTeams(): LiveData<List<com.multiplexer.cricapp.models.teams.Data>>

    @Query("SELECT * FROM leagues")
    fun getAllLeagues(): LiveData<List<com.multiplexer.cricapp.models.leagues.Data>>

    @Query("SELECT name FROM teams WHERE id = :id")
    fun getTeamName(id: Long): LiveData<String>

    @Query("SELECT name FROM leagues WHERE id = :id")
    fun getLeagueName(id: Long): LiveData<String>

    @Query("SELECT name FROM countries WHERE id = :id")
    fun getCountryName(id: Int): LiveData<String>

    @Query("SELECT image_path FROM teams WHERE id = :id")
    fun getImageFromTeam(id: Long): LiveData<String>

    @Query("SELECT * FROM countries")
    fun getALlCountry(): LiveData<List<com.multiplexer.cricapp.models.countries.Data>>

    @Query("SELECT * FROM teams where country_id = :id")
    fun getALlTeamsWithCountryID(id: Int): LiveData<List<com.multiplexer.cricapp.models.teams.Data>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCountries(countries: List<com.multiplexer.cricapp.models.countries.Data>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllContinents(continents: List<com.multiplexer.cricapp.models.continents.Data>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllTeams(continents: List<com.multiplexer.cricapp.models.teams.Data>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllLeagues(continents: List<com.multiplexer.cricapp.models.leagues.Data>)
}