package com.multiplexer.cricapp.models.fixtures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Run(
    val fixture_id: Int?,
    val id: Int?,
    val inning: Int?,
    val overs: Double?,
    val pp1: String?,
    val pp2: @RawValue Any?,
    val pp3: @RawValue Any?,
    val resource: String?,
    val score: Int?,
    val team_id: Int?,
    val updated_at: String?,
    val wickets: Int?
) : Parcelable