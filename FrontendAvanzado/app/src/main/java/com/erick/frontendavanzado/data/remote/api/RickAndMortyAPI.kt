package com.erick.frontendavanzado.data.remote.api

import com.erick.frontendavanzado.data.remote.dto.AllCharactersResponse
import com.erick.frontendavanzado.data.remote.dto.CharacterDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyAPI {

    @GET("/api/character")
    suspend fun getCharacters() : AllCharactersResponse
    @GET("/api/character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ) : CharacterDto

}