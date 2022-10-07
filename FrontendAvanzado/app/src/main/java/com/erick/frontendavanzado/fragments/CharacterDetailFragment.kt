package com.erick.frontendavanzado.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.erick.frontendavanzado.R
import com.erick.frontendavanzado.activities.MainActivity
import com.erick.frontendavanzado.data.api_source.api.RetrofitInstance
import com.erick.frontendavanzado.data.api_source.model.CharacterDto
import com.erick.frontendavanzado.data.local_source.Database
import com.erick.frontendavanzado.domain.model.Character
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private lateinit var profilePicture: ImageView
    private lateinit var name: TextInputLayout
    private lateinit var species: TextInputLayout
    private lateinit var toolBar: MaterialToolbar
    private lateinit var btnGuardar: MaterialButton
    private lateinit var status: TextInputLayout
    private lateinit var gender: TextInputLayout
    private lateinit var origin: TextInputLayout
    private lateinit var appearances: TextInputLayout
    private lateinit var database: Database
    private lateinit var currentCharacter: Character

    val args: CharacterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar = (activity as MainActivity).getToolBar()
        btnGuardar = view.findViewById(R.id.btn_characterDetailFragment_guardar)
        profilePicture = view.findViewById(R.id.img_profilePicture_characterDetailFragment)
        name = view.findViewById(R.id.txt_characterName)
        species = view.findViewById(R.id.txt_speciesInfo)
        status = view.findViewById(R.id.txt_statusInfo)
        gender = view.findViewById(R.id.txt_genderInfo)
        origin = view.findViewById(R.id.txt_originInfo)
        appearances = view.findViewById(R.id.txt_appearancesInfo)
        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dbCharacters"
        ).build()

        System.out.println(args.characterId)

        CoroutineScope(Dispatchers.IO).launch {
            currentCharacter = database.characterDao().getCharacter(id=args.characterId)
            setData()
        }

        setListeners()
    }

    private fun updateCharacter(){
        val updatedCharacter = currentCharacter.copy(
            episodes = appearances.editText!!.text.toString().toInt(),
            gender = gender.editText!!.text.toString(),
            name = name.editText!!.text.toString(),
            origin = origin.editText!!.text.toString(),
            species = species.editText!!.text.toString(),
            status = status.editText!!.text.toString()
        )
        CoroutineScope(Dispatchers.IO).launch {
            database.characterDao().update(updatedCharacter)
        }
    }

    private fun setListeners() {
        btnGuardar.setOnClickListener {
            updateCharacter()
        }
        toolBar.setOnMenuItemClickListener{menuItem->
            when(menuItem.itemId){
                R.id.menu_item_update->{
                    reloadCharacter()
                    true
                }
                R.id.menu_item_delete->{
                    deleteCharacter()
                    true
                }
                else -> true
            }
        }
    }

    private fun deleteCharacter(){
        CoroutineScope(Dispatchers.IO).launch {
            database.characterDao().delete(currentCharacter)
        }
    }

    private fun reloadCharacter() {
        RetrofitInstance.api.getCharacter(args.characterId).enqueue(object : Callback<CharacterDto>{
            override fun onResponse(call: Call<CharacterDto>, response: Response<CharacterDto>) {
                if(response.isSuccessful && response.body() != null){
                    profilePicture.load(response.body()!!.image){
                        crossfade(true)
                        crossfade(500)
                        placeholder(R.drawable.ic_loading)
                        error(R.drawable.ic_error)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        diskCachePolicy(CachePolicy.ENABLED)
                        transformations(CircleCropTransformation())
                    }
                    name.editText!!.setText(response.body()!!.name)
                    species.editText!!.setText(response.body()!!.species)
                    status.editText!!.setText(response.body()!!.status)
                    gender.editText!!.setText(response.body()!!.gender)
                    origin.editText!!.setText(response.body()!!.origin.name)
                    appearances.editText!!.setText(response.body()!!.episode.size.toString())
                    updateCharacter()
                }
            }

            override fun onFailure(call: Call<CharacterDto>, t: Throwable) {
                println("Error")
            }

        })
    }

    private fun setData(){
        CoroutineScope(Dispatchers.Main).launch {
            profilePicture.load(currentCharacter.image){
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_error)
                memoryCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED)
                transformations(CircleCropTransformation())
            }
            name.editText!!.setText(currentCharacter.name)
            species.editText!!.setText(currentCharacter.species)
            status.editText!!.setText(currentCharacter.status)
            gender.editText!!.setText(currentCharacter.gender)
            origin.editText!!.setText(currentCharacter.origin)
            appearances.editText!!.setText(currentCharacter.episodes.toString())
        }
    }
}