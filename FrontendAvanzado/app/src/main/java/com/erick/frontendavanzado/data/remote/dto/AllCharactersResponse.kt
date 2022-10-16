package com.erick.frontendavanzado.data.remote.dto

data class AllCharactersResponse(
    val info: Info,
    val results: MutableList<CharacterDto>
)