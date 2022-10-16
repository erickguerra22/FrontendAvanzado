package com.erick.frontendavanzado.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character (
    @PrimaryKey
    var id:Int,
    val episodes: Int,
    val gender: String,
    val image: String,
    val name: String,
    val origin: String,
    val species: String,
    val status: String
)