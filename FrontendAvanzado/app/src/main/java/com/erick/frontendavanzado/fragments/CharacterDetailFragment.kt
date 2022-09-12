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

class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private lateinit var profilePicture: ImageView
    private lateinit var name: TextView
    private lateinit var species: TextView
    private lateinit var status: TextView
    private lateinit var gender: TextView

    val args: CharacterDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePicture = view.findViewById(R.id.img_profilePicture_characterDetailFragment)
        name = view.findViewById(R.id.txt_characterName)
        species = view.findViewById(R.id.txt_speciesInfo)
        status = view.findViewById(R.id.txt_statusInfo)
        gender = view.findViewById(R.id.txt_genderInfo)

        profilePicture.load(args.image){
            crossfade(true)
            crossfade(500)
            placeholder(R.drawable.ic_loading)
            error(R.drawable.ic_error)
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)
            transformations(CircleCropTransformation())
        }
        name.text = args.name
        species.text = args.species
        status.text = args.status
        gender.text = args.gender
    }
}