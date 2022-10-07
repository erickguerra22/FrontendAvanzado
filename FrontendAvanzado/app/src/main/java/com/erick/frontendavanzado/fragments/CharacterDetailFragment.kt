package com.erick.frontendavanzado.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.erick.frontendavanzado.R
import com.erick.frontendavanzado.data.api_source.api.RetrofitInstance
import com.erick.frontendavanzado.data.api_source.model.CharacterDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private lateinit var profilePicture: ImageView
    private lateinit var name: TextView
    private lateinit var species: TextView
    private lateinit var status: TextView
    private lateinit var gender: TextView
    private lateinit var origin: TextView
    private lateinit var appearances: TextView

    val args: CharacterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePicture = view.findViewById(R.id.img_profilePicture_characterDetailFragment)
        name = view.findViewById(R.id.txt_characterName)
        species = view.findViewById(R.id.txt_speciesInfo)
        status = view.findViewById(R.id.txt_statusInfo)
        gender = view.findViewById(R.id.txt_genderInfo)
        origin = view.findViewById(R.id.txt_originInfo)
        appearances = view.findViewById(R.id.txt_appearancesInfo)
        val characterId = args.characterId

        RetrofitInstance.api.getCharacter(characterId).enqueue(object : Callback<CharacterDto>{
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
                    name.text = response.body()!!.name
                    species.text = response.body()!!.species
                    status.text = response.body()!!.status
                    gender.text = response.body()!!.gender
                    origin.text = response.body()!!.origin.name
                    appearances.text = response.body()!!.episode.size.toString()
                }
            }

            override fun onFailure(call: Call<CharacterDto>, t: Throwable) {
                println("Error")
            }

        })
    }
}