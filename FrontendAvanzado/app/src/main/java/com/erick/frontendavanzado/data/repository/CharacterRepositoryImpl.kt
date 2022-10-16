package com.erick.frontendavanzado.data.repository

import com.erick.frontendavanzado.data.local.dao.CharacterDao
import com.erick.frontendavanzado.data.local.model.Character
import com.erick.frontendavanzado.data.remote.api.RickAndMortyAPI
import com.erick.frontendavanzado.data.remote.dto.mapToModel
import com.erick.frontendavanzado.data.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterRepositoryImpl(
    private val characterDao: CharacterDao,
    private var api: RickAndMortyAPI
) : CharacterRepository {
    override fun getAllCharacters(): Flow<DataState<List<Character>>> = flow {
        val localCharacters = characterDao.getAllCharacters()
        if(localCharacters.isEmpty()){
            try {
                val remoteCharacters = api.getCharacters().results
                val charactersToStore = remoteCharacters.map { dto -> dto.mapToModel() }
                characterDao.insertAll(charactersToStore)
                emit(DataState.Success(charactersToStore))
            }catch (e: Exception){
                emit(DataState.Error(e))
            }
        }else{
            emit(DataState.Success(localCharacters))
        }
    }

    override fun deleteAllCharacters(): Flow<DataState<Int>> = flow{
        val deletedCharacters = characterDao.deleteAll()

    }

    override fun getCharacter(id: Int): Flow<DataState<Character?>> = flow{
        val localCharacter = characterDao.getCharacter(id)

    }

    override fun updateCharacter(character: Character): Flow<DataState<Int>> {
        TODO("Not yet implemented")
    }

    override fun deleteCharacter(id: Int): Flow<DataState<Int>> {
        TODO("Not yet implemented")
    }


}