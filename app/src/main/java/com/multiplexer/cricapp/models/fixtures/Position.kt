package com.multiplexer.cricapp.models.fixtures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Position(
    val id: Int?,
    val name: String?,
    val resource: String?
) : Parcelable