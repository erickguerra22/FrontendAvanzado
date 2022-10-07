package com.erick.frontendavanzado.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.erick.frontendavanzado.R
import com.erick.frontendavanzado.domain.model.Character

class CharacterAdapter(
    private val dataSet: MutableList<Character>,
    private val listener: RecyclerCharacterClickHandler
    ) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {


    class CharacterViewHolder(private val view: View,
                              private val listener: RecyclerCharacterClickHandler
                              ): RecyclerView.ViewHolder(view){
        private val imgCharacter: ImageView = view.findViewById(R.id.img_characterItem)
        private val nameCharacter: TextView = view.findViewById(R.id.txt_characterItemName)
        private val infoCharacter: TextView = view.findViewById(R.id.txt_characterItemInfo)
        private val layoutCharacter: ConstraintLayout = view.findViewById(R.id.layout_itemCharacter)

        fun setData(character: Character){
            imgCharacter.load(character.image){
                crossfade(true)
                crossfade(500)
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_error)
                transformations(CircleCropTransformation())
                memoryCachePolicy(CachePolicy.DISABLED)
                diskCachePolicy(CachePolicy.DISABLED)
            }
            nameCharacter.text = character.name
            infoCharacter.text = "${character.species} - ${character.status}"
            layoutCharacter.setOnClickListener{
                listener.onCharacterClicked(character)
            }
        }
    }

    interface RecyclerCharacterClickHandler {
        fun onCharacterClicked(character: Character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_character, parent, false)

        return CharacterViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.setData(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}