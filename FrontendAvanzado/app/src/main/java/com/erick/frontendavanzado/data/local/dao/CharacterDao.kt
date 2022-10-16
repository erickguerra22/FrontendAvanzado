package com.erick.frontendavanzado.data.local.dao

import androidx.room.*
import com.erick.frontendavanzado.data.local.model.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM character")
    suspend fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getCharacter(id:Int): Character

    @Insert
    suspend fun insert(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Update
    suspend fun update(character: Character)

    @Delete
    suspend fun delete(character: Character): Int

    @Query("DELETE FROM character")
    suspend fun deleteAll(): Int
}