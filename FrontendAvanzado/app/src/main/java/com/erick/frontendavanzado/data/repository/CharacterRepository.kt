package com.erick.frontendavanzado.data.repository

import com.erick.frontendavanzado.data.local.model.Character
import com.erick.frontendavanzado.data.util.DataState
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(): Flow<DataState<List<Character>>>
    fun deleteAllCharacters(): Flow<DataState<Int>>
    fun getCharacter(id: Int): Flow<DataState<Character?>>
    fun updateCharacter(character: Character): Flow<DataState<Int>>
    fun deleteCharacter(id: Int): Flow<DataState<Int>>
}