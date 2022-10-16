package com.erick.frontendavanzado.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erick.frontendavanzado.data.local.dao.CharacterDao
import com.erick.frontendavanzado.data.local.model.Character

@Database(entities = [Character::class], version = 1)
abstract class Database : RoomDatabase(){
    abstract fun characterDao(): CharacterDao
}