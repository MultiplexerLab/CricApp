package com.multiplexer.cricapp.models.continents

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "continents")
data class Data(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val resource: String?,
    val updated_at: String?
)