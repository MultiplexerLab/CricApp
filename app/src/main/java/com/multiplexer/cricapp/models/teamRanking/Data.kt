package com.multiplexer.cricapp.models.teamRanking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Data(
    val gender: String?,
    val points: @RawValue Any?,
    val position: @RawValue Any?,
    val rating: @RawValue Any?,
    val resource: String?,
    val team: @RawValue List<Team>?,
    val type: String?,
    val updated_at: String?
) : Parcelable