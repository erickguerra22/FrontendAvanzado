package com.erick.frontendavanzado.data.remote.dto

import com.erick.frontendavanzado.data.local.model.Character

data class CharacterDto(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

fun CharacterDto.mapToModel(): Character = Character(
    id = id.toInt(),
    episodes = episode.size,
    gender = gender,
    image = image,
    name = name,
    origin = origin.name,
    species = species,
    status = status
)