package com.multiplexer.cricapp.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.*
import com.multiplexer.cricapp.models.fixtures.Data
import com.multiplexer.cricapp.models.teamWithSquad.Squad
import com.multiplexer.cricapp.repositories.*
import com.multiplexer.cricapp.services.CricketAPI
import com.multiplexer.cricapp.utlis.Constants
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "CricketViewModel"

class CricketViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    val context: Context
    private val _countries = MutableLiveData<List<com.multiplexer.cricapp.models.countries.Data>>()
    var countries: LiveData<List<com.multiplexer.cricapp.models.countries.Data>> = _countries

    private val _continents = MutableLiveData<List<com.multiplexer.cricapp.models.continents.Data>>()
    var continents: LiveData<List<com.multiplexer.cricapp.models.continents.Data>> = _continents

    private val _teams = MutableLiveData<List<com.multiplexer.cricapp.models.teams.Data>>()
    var teams: LiveData<List<com.multiplexer.cricapp.models.teams.Data>> = _teams

    private val _leagues = MutableLiveData<List<com.multiplexer.cricapp.models.leagues.Data>>()
    var leagues: LiveData<List<com.multiplexer.cricapp.models.leagues.Data>> = _leagues

    private val _squads = MutableLiveData<List<Squad>>()
    var squads: LiveData<List<Squad>> = _squads

    private val _fixtures = MutableLiveData<List<Data>>()
    var fixtures: LiveData<List<Data>> = _fixtures

    private val _liveFixtures = MutableLiveData<List<Data>>()
    var liveFixtures: LiveData<List<Data>> = _liveFixtures

    private val _cachedFixtures = MutableLiveData<List<Data>>()
    val cachedFixtures: LiveData<List<Data>>
        get() = _cachedFixtures

    private val _cachedFixturesWithLeague = MutableLiveData<List<Data>>()
    val cachedFixturesWithLeague: LiveData<List<Data>>
        get() = _cachedFixturesWithLeague

    private val _cachedFixturesWithTeam = MutableLiveData<List<Data>>()
    val cachedFixturesWithTeam: LiveData<List<Data>>
        get() = _cachedFixturesWithTeam

    private val _cachedLiveFixtures = MutableLiveData<List<Data>>()
    val cachedLiveFixtures: LiveData<List<Data>>
        get() = _cachedLiveFixtures

    private var _cachedPlayerDetails = MutableLiveData<com.multiplexer.cricapp.models.palyers.Data>()
    val cachedPlayerDetails: LiveData<com.multiplexer.cricapp.models.palyers.Data>
        get() = _cachedPlayerDetails

    private var _cachedPlayerList =
        MutableLiveData<List<com.multiplexer.cricapp.models.playersList.Data>>()
    val cachedPlayerList: LiveData<List<com.multiplexer.cricapp.models.playersList.Data>>
        get() = _cachedPlayerList

    private var _cachedTeamsList =
        MutableLiveData<List<com.multiplexer.cricapp.models.teamRanking.Data>>()
    val cachedTeamsList: LiveData<List<com.multiplexer.cricapp.models.teamRanking.Data>>
        get() = _cachedTeamsList


    private var cricketRepo: CricketRepository

    init {
        context = application.applicationContext
        cricketRepo = CricketRepository(application)

        CoroutineScope(Dispatchers.Default).launch {
            getContinents()
            getCountries()
            getTeams()
            getAllLeague()
            //getSquadByTeam()
        }
    }

    fun getLiveFixtures() {
        LiveFixturesRepository().getLiveFixtures(
            "runs, lineup, venue, manofmatch, batting, bowling", Constants.api_key
        ) {
            _cachedLiveFixtures.value = it
        }
    }

    fun getPlayerDetails(id: Int) {
        PlayerInfoRepository().getPlayerInfo(id, "career,teams", Constants.api_key) {
            _cachedPlayerDetails.value = it
        }
    }

    fun getPlayerList(positionId: Int) {
        PlayerListRepository().getPlayerInfo(
            positionId,
            "firstname,lastname,country_id,dateofbirth,fullname,image_path",
            Constants.api_key
        ) {
            _cachedPlayerList.value = it
        }
    }

    fun getFixturesWithLeague(leagueId: Int) {
        val date = getDateRange(365)
        FixturesWithLeagueRepository().getFixturesWithLeagueId(
            leagueId,
            date,
            "runs, lineup, venue, manofmatch, batting, bowling,batting.batsman, bowling.bowler",
            "-starting_at",
            Constants.api_key
        ) {
            _cachedFixturesWithLeague.value = it
        }
    }

    fun getFixturesWithTeam(teamID: Int) {
        val date = getDateRange(180)
        FixturesWithTeamRepository().getFixturesWithTeamId(
            teamID,
            date,
            "runs, lineup, venue, manofmatch, batting, bowling,batting.batsman, bowling.bowler",
            "-starting_at",
            Constants.api_key
        ) {
            _cachedFixturesWithTeam.value = it
        }
    }

    fun getFixtures(varDate: Int) {
        val date = getDate(varDate)
        FixturesRepository().getFixtures(
            date,
            "runs, lineup, venue, manofmatch, batting, bowling,batting.batsman, bowling.bowler",
            "Finished,Aban.,NS,1st Innings, 2nd Innings,Int., 3rd Innings, 4th Innings,Stump Day 1,Stump Day 2,Stump Day 3,Stump Day 4,Innings Break",
            "-starting_at",
            Constants.api_key
        ) {
            _cachedFixtures.value = it
        }
    }

    fun getTeamWithRanking() {
        TeamsRepository().getTeamWithRank(
            Constants.api_key
        ) {
            _cachedTeamsList.value = it
        }
    }

    private fun getAllLeague() {
        if (cricketRepo.getAllLeague().value?.size != 0) {
            viewModelScope.launch {
                try {
                    _leagues.value = CricketAPI.retrofitService.getAllLeague(Constants.api_key).data
                } catch (e: Exception) {
                    Log.d("country", e.message.toString())
                }
            }
        }
    }

    private fun getTeams() {
        if (cricketRepo.getAllTeams().value?.size != 0) {
            viewModelScope.launch {
                try {
                    _teams.value = CricketAPI.retrofitService.getAllTeams(Constants.api_key).data
                } catch (e: Exception) {
                    Log.d("country", e.message.toString())
                }
            }
        }
    }

    private fun getContinents() {
        if (cricketRepo.getAllContinents().value?.size != 0) {
            viewModelScope.launch {
                try {
                    _continents.value =
                        CricketAPI.retrofitService.getAllContinents(Constants.api_key).data
                } catch (e: Exception) {
                    Log.d("country", e.message.toString())
                }
            }
        }
    }

    private fun getCountries() {
        if (cricketRepo.getAllCountries().value?.size != 0) {
            viewModelScope.launch {
                try {
                    _countries.value =
                        CricketAPI.retrofitService.getAllCountries(Constants.api_key).data
                } catch (e: Exception) {
                    Log.d("country", e.message.toString())
                }
            }
        }
    }

    private fun getDate(diff: Int): String {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate)
        calendar.add(Calendar.DATE, diff)
        val nextDayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        return if (diff == 0) {
            "${currentDate}T00:00:00.000000Z,${currentDate}T23:59:00.000000Z"
        } else {
            "${nextDayDate}T00:00:00.000000Z,${nextDayDate}T23:59:00.000000Z"
        }
    }

    private fun getDateRange(diff: Int): String {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate)
        calendar.add(Calendar.DATE, -diff)
        val nextDayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        return "${nextDayDate}T00:00:00.000000Z,${currentDate}T23:59:00.000000Z"
    }

    fun addAllCountry(article: List<com.multiplexer.cricapp.models.countries.Data>) {
        viewModelScope.launch(Dispatchers.IO) {
            cricketRepo.insertAllCountry(article)
        }
    }

    fun getAllCountries(): LiveData<List<com.multiplexer.cricapp.models.countries.Data>> {
        return cricketRepo.getAllCountries()
    }


    fun addAllContinents(article: List<com.multiplexer.cricapp.models.continents.Data>) {
        viewModelScope.launch(Dispatchers.IO) {
            cricketRepo.insertAllContinents(article)
        }
    }

    fun addAllTeams(article: List<com.multiplexer.cricapp.models.teams.Data>) {
        viewModelScope.launch(Dispatchers.IO) {
            cricketRepo.insertAllTeams(article)
        }
    }

    fun addAllLeague(league: List<com.multiplexer.cricapp.models.leagues.Data>) {
        viewModelScope.launch(Dispatchers.IO) {
            cricketRepo.insertAllLeague(league)
        }
    }

    fun getAllContinents(): LiveData<List<com.multiplexer.cricapp.models.continents.Data>> {
        return cricketRepo.getAllContinents()
    }

    fun getAllTeams(): LiveData<List<com.multiplexer.cricapp.models.teams.Data>> {
        return cricketRepo.getAllTeams()
    }

    fun getAllLeagues(): LiveData<List<com.multiplexer.cricapp.models.leagues.Data>> {
        return cricketRepo.getAllLeague()
    }

    fun getTeamName(teamId: Long): LiveData<String> {
        return cricketRepo.getTeamName(teamId)
    }


    fun getLeagueName(leagueId: Long): LiveData<String> {
        return cricketRepo.getLeagueName(leagueId)
    }

    fun getCountryName(countryId: Int): LiveData<String> {
        return cricketRepo.getCountryName(countryId)
    }

    fun getImageFromTeam(teamId: Long): LiveData<String> {
        return cricketRepo.getImageFromTeam(teamId)
    }


}