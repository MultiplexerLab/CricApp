package com.multiplexer.cricapp.models.fakeFixture

import com.multiplexer.cricapp.models.palyers.InSquad

data class Team(
    val code: String,
    val country_id: Int,
    val id: Int,
    val image_path: String,
    val in_squad: InSquad,
    val name: String,
    val national_team: Boolean,
    val resource: String,
    val updated_at: String
)