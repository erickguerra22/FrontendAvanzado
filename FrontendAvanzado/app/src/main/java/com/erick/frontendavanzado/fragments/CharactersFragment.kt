package com.erick.frontendavanzado.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erick.frontendavanzado.R
import com.erick.frontendavanzado.activities.MainActivity
import com.erick.frontendavanzado.adapters.CharacterAdapter
import com.erick.frontendavanzado.datasource.api.RetrofitInstance
import com.erick.frontendavanzado.datasource.model.AllCharactersResponse
import com.erick.frontendavanzado.datasource.model.CharacterDto
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerCharacterClickHandler {

    private lateinit var recyclerView: RecyclerView
    private lateinit var characterList: MutableList<CharacterDto>
    private lateinit var toolBar: MaterialToolbar
    private lateinit var adapter: CharacterAdapter
    private lateinit var loginFragment: LoginFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar = (activity as MainActivity).getToolBar()
        recyclerView = view.findViewById(R.id.recyclerView_charactersFragment)
        loginFragment = LoginFragment()

        RetrofitInstance.api.getCharacters().enqueue(object : Callback<AllCharactersResponse>{
            override fun onResponse(
                call: Call<AllCharactersResponse>,
                response: Response<AllCharactersResponse>
            ) {
                if(response.isSuccessful && response.body() != null){
                    characterList = response.body()!!.results
                    setupRecycler()
                }
            }

            override fun onFailure(call: Call<AllCharactersResponse>, t: Throwable) {
                println("Error")
            }

        })

        setListeners()
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
                        loginFragment.deleteMail(requireContext())
                    }
                    requireView().findNavController().navigate(R.id.action_charactersFragment_to_loginFragment)
                    true
                }
                else -> true
            }
        }
    }

    private fun setupRecycler() {
        adapter = CharacterAdapter(characterList, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onCharacterClicked(character: CharacterDto) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
            character.id)
        requireView().findNavController().navigate(action)
    }
}