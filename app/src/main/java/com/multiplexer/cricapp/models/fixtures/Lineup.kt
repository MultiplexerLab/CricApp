package com.multiplexer.cricapp.models.fixtures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Lineup(
    val battingstyle: String?,
    val bowlingstyle: String?,
    val country_id: Int?,
    val dateofbirth: String?,
    val firstname: String?,
    val fullname: String?,
    val gender: String?,
    val id: Int?,
    val image_path: String?,
    val lastname: String?,
    val lineup:@RawValue LineupX?,
    val position:@RawValue Position?,
    val resource: String?,
    val updated_at: String?
) : Parcelable