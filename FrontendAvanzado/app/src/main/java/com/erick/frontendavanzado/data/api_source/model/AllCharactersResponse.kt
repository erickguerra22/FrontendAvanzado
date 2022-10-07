package com.erick.frontendavanzado.data.api_source.model

data class AllCharactersResponse(
    val info: Info,
    val results: MutableList<CharacterDto>
)