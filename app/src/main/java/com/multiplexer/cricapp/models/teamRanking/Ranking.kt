package com.multiplexer.cricapp.models.teamRanking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ranking(
    val matches: Int?,
    val points: Int?,
    val position: Int?,
    val rating: Int?
) : Parcelable