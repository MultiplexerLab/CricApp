package com.multiplexer.cricapp.models.palyers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Career(
    val batting: @RawValue Batting?,
    val bowling: @RawValue Bowling?,
    val player_id: Int?,
    val resource: String?,
    val season_id: Int?,
    val type: String?,
    val updated_at: String?
) : Parcelable