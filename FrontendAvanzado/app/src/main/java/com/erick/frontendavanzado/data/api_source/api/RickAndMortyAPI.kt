package com.erick.frontendavanzado.data.api_source.api

import com.erick.frontendavanzado.data.api_source.model.AllCharactersResponse
import com.erick.frontendavanzado.data.api_source.model.CharacterDto
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