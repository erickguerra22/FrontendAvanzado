package com.erick.frontendavanzado.datasource.model

data class AllCharactersResponse(
    val info: Info,
    val results: MutableList<CharacterDto>
)