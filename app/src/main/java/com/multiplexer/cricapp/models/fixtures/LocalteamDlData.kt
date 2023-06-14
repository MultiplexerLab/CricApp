package com.multiplexer.cricapp.models.fixtures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalteamDlData(
    val overs: String?,
    val rpc_overs: String?,
    val rpc_targets: String?,
    val score: String?,
    val wickets_out: String?
) : Parcelable