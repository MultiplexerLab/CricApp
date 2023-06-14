package com.multiplexer.cricapp.models.palyers

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.multiplexer.cricapp.models.fakeFixture.Team
import com.multiplexer.cricapp.models.teamWithSquad.SquadX
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = "players")
data class Data(
    val battingstyle: String?,
    val bowlingstyle: String?,
    val country_id: Int?,
    val dateofbirth: String?,
    val firstname: String?,
    val fullname: String?,
    val gender: String?,
    val teams: @RawValue List<Team>?,
    @PrimaryKey
    val id: Int?,
    val image_path: String?,
    val lastname: String?,
    val position: @RawValue Position?,
    val resource: String?,
    val squad: @RawValue SquadX?,
    val career: @RawValue List<Career>?,
    val updated_at: String?,
) : Parcelable