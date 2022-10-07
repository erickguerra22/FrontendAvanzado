package com.erick.frontendavanzado.data.local_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erick.frontendavanzado.domain.model.Character

@Database(entities = [Character::class], version = 1)
abstract class Database : RoomDatabase(){
    abstract fun characterDao(): CharacterDao
}