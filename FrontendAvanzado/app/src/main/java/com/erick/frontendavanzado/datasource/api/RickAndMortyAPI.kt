package com.erick.frontendavanzado.datasource.api

import com.erick.frontendavanzado.datasource.model.AllCharactersResponse
import com.erick.frontendavanzado.datasource.model.CharacterDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RickAndMortyAPI {

    @GET("/api/character")
    fun getCharacters() : Call<AllCharactersResponse>
    @GET("/api/character/{id}")
    fun getCharacter(
        @Path("id") id: Int
    ) : Call<CharacterDto>

}