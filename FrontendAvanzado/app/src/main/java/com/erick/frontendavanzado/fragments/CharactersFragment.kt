package com.erick.frontendavanzado.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.erick.frontendavanzado.R
import com.erick.frontendavanzado.activities.MainActivity
import com.erick.frontendavanzado.adapters.CharacterAdapter
import com.erick.frontendavanzado.data.api_source.api.RetrofitInstance
import com.erick.frontendavanzado.data.api_source.model.AllCharactersResponse
import com.erick.frontendavanzado.data.api_source.model.CharacterDto
import com.erick.frontendavanzado.data.local_source.Database
import com.erick.frontendavanzado.domain.model.Character
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerCharacterClickHandler {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolBar: MaterialToolbar
    private lateinit var adapter: CharacterAdapter
    private lateinit var loginFragment: LoginFragment
    private lateinit var database: Database
    private var characterList: MutableList<Character> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar = (activity as MainActivity).getToolBar()
        recyclerView = view.findViewById(R.id.recyclerView_charactersFragment)
        loginFragment = LoginFragment()
        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dbCharacters"
        ).build()

        getCharacters()
        setListeners()
    }

    private fun saveCharacters(characterList: MutableList<Character>) {
        characterList.forEach{
            CoroutineScope(Dispatchers.IO).launch {
                database.characterDao().insert(it)
            }
        }
    }

    private fun getCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            val characters = database.characterDao().getAllCharacters()
            if(characters.isEmpty())
                getDataFromAPI()
            else{
                characterList.clear()
                characterList.addAll(characters)
                CoroutineScope(Dispatchers.Main).launch {
                    setupRecycler()
                }
            }
        }
    }

    private fun getDataFromAPI() {
        characterList.clear()
        RetrofitInstance.api.getCharacters().enqueue(object : Callback<AllCharactersResponse>{
            override fun onResponse(
                call: Call<AllCharactersResponse>,
                response: Response<AllCharactersResponse>
            ) {
                if(response.isSuccessful && response.body() != null){
                    characterList = toCharacter(response.body()!!.results)
                    saveCharacters(characterList)
                    setupRecycler()
                }
            }

            override fun onFailure(call: Call<AllCharactersResponse>, t: Throwable) {
                println("Error")
            }

        })
    }

    private fun toCharacter(results: MutableList<CharacterDto>): MutableList<Character> {
        var characters = mutableListOf<Character>()
        results.forEach{
            val newCharacter = Character(
                it.id,
                it.episode.size,
                it.gender,
                it.image,
                it.name,
                it.origin.name,
                it.species,
                it.status
            )
            characters.add(newCharacter)
        }
        return characters
    }

    private fun setListeners() {
        toolBar.setOnMenuItemClickListener{menuItem->
            when(menuItem.itemId){
                R.id.menu_item_sortAZ ->{
                    characterList.sortBy { character -> character.name }
                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.menu_item_sortZA ->{
                    characterList.sortByDescending { character -> character.name }
                    adapter.notifyDataSetChanged()
                    true
                }
                R.id.menu_item_logOut ->{
                    CoroutineScope(Dispatchers.IO).launch {
                        database.characterDao().deleteAll()
                        loginFragment.deleteMail(requireContext())
                    }
                    requireView().findNavController().navigate(R.id.action_charactersFragment_to_loginFragment)
                    true
                }
                R.id.menu_item_update->{
                    syncData()
                    true
                }
                else -> true
            }
        }
    }

    private fun syncData() {
        RetrofitInstance.api.getCharacters().enqueue(object : Callback<AllCharactersResponse>{
            override fun onResponse(
                call: Call<AllCharactersResponse>,
                response: Response<AllCharactersResponse>
            ) {
                if(response.isSuccessful && response.body() != null){
                    CoroutineScope(Dispatchers.IO).launch {
                        database.characterDao().deleteAll()
                    }
                    CoroutineScope(Dispatchers.Main).launch{
                        characterList.clear()
                        characterList = toCharacter(response.body()!!.results)
                        addToDatabase(characterList)
                        setupRecycler()
                    }
                }
            }

            override fun onFailure(call: Call<AllCharactersResponse>, t: Throwable) {
                println("Error")
            }

        })
    }

    private fun addToDatabase(characterList: MutableList<Character>) {
        characterList.forEach{
            CoroutineScope(Dispatchers.IO).launch {
                database.characterDao().insert(it)
            }
        }
    }

    private fun setupRecycler() {
        adapter = CharacterAdapter(characterList, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onCharacterClicked(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
            character.id)
        requireView().findNavController().navigate(action)
    }
}