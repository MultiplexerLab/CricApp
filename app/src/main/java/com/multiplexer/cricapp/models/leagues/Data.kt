package com.multiplexer.cricapp.models.leagues

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "leagues")
data class Data(
    val code: String?,
    val country_id: Int?,
    @PrimaryKey
    val id: Int?,
    val image_path: String?,
    val name: String?,
    val resource: String?,
    val season_id: Int?,
    val type: String?,
    val updated_at: String?
) : Parcelable