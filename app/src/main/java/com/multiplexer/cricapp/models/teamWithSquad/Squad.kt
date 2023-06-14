package com.multiplexer.cricapp.models.teamWithSquad

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Squad(
    var battingstyle: String?,
    var bowlingstyle: String?,
    var country_id: Int?,
    var dateofbirth: String?,
    var firstname: String?,
    var fullname: String?,
    var gender: String?,
    var id: Int?,
    var image_path: String?,
    var lastname: String?,
    val position: @RawValue Position?,
    var resource: String?,
    val squad: @RawValue SquadX?,
    var updated_at: String?
) : Parcelable