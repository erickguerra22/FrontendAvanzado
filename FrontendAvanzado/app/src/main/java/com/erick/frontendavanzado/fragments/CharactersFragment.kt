package com.erick.frontendavanzado.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erick.frontendavanzado.R
import com.erick.frontendavanzado.activities.MainActivity
import com.erick.frontendavanzado.adapters.CharacterAdapter
import com.erick.frontendavanzado.database.Database
import com.erick.frontendavanzado.entities.Character
import com.google.android.material.appbar.MaterialToolbar

class CharactersFragment : Fragment(R.layout.fragment_characters), CharacterAdapter.RecyclerCharacterClickHandler {

    private lateinit var recyclerView: RecyclerView
    private lateinit var characterList: MutableList<Character>
    private lateinit var toolBar: MaterialToolbar
    private lateinit var adapter: CharacterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar = (activity as MainActivity).getToolBar()
        recyclerView = view.findViewById(R.id.recyclerView_charactersFragment)
        setupRecycler()
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
                else -> true
            }
        }
    }

    private fun setupRecycler() {
        characterList = Database.getCharacters()
        adapter = CharacterAdapter(characterList, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onCharacterClicked(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
            character.image, character.name, character.species, character.status, character.gender)
        requireView().findNavController().navigate(action)
    }

}