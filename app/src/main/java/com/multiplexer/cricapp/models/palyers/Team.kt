package com.multiplexer.cricapp.models.palyers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Team(
    val code: String?,
    val country_id: Int?,
    val id: Int?,
    val image_path: String?,
    val in_squad: @RawValue InSquad?,
    val name: String?,
    val national_team: Boolean?,
    val resource: String?,
    val updated_at: String?
) : Parcelable