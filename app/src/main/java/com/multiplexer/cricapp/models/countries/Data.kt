package com.multiplexer.cricapp.models.countries

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class Data(
    val continent_id: Int?,
    @PrimaryKey
    val id: Int?,
    val image_path: String?,
    val name: String?,
    val resource: String?,
    val updated_at: String?
)