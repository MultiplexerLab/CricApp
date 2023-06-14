package com.multiplexer.cricapp.models.fixtures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineupX(
    val captain: Boolean?,
    val substitution: Boolean?,
    val team_id: Int?,
    val wicketkeeper: Boolean?
) : Parcelable