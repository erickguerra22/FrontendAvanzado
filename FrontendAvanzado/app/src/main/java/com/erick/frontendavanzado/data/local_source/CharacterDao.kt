package com.erick.frontendavanzado.data.local_source

import androidx.room.*
import com.erick.frontendavanzado.domain.model.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM character")
    suspend fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getCharacter(id:Int): Character

    @Insert
    suspend fun insert(character: Character)

    @Update
    suspend fun update(character: Character)

    @Delete
    suspend fun delete(character: Character): Int

    @Query("DELETE FROM character")
    suspend fun deleteAll(): Int
}