package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CharactersAdapter(val context: Context, val charactersList: List<Long>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.characters_layout, parent, false)
        return CharacterViewHolder(view)

    }

    class CharacterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.card)!!
        val name = view.findViewById<TextView>(R.id.tv)!!
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPictureId = charactersList[position]
        val viewHolder = holder as CharacterViewHolder
        val drawableMap = mapOf(
            "s0" to R.drawable.s0,
            "s1" to R.drawable.s1,
            "s2" to R.drawable.s2,
            "s3" to R.drawable.s3,
            "s4" to R.drawable.s4,
            "s5" to R.drawable.s5,
            "s6" to R.drawable.s6,
            "s7" to R.drawable.s7,
            "s8" to R.drawable.s8,
            "s9" to R.drawable.s9,
            "s10" to R.drawable.s10,
            "s11" to R.drawable.s11,
            "s12" to R.drawable.s12,
            "s13" to R.drawable.s13,
            "s14" to R.drawable.s14,
            "s15" to R.drawable.s15,
            "s16" to R.drawable.s16,
            "s17" to R.drawable.s17,
            "s18" to R.drawable.s18,
            "s19" to R.drawable.s19,
            "s20" to R.drawable.s20,
            "s21" to R.drawable.s21,
            "s22" to R.drawable.s22,
            "s23" to R.drawable.s23,
            "s24" to R.drawable.s24,
            "s25" to R.drawable.s25,
            "s26" to R.drawable.s26,
            "s27" to R.drawable.s27,
            "s28" to R.drawable.s28,
            "s29" to R.drawable.s29,
            "s30" to R.drawable.s30,
            "s31" to R.drawable.s31,
            "s32" to R.drawable.s32,
            "s33" to R.drawable.s33,
            "s34" to R.drawable.s34,
            "s35" to R.drawable.s35,
            "s36" to R.drawable.s36,
            "s37" to R.drawable.s37,
            "s38" to R.drawable.s38,
            "s39" to R.drawable.s39,
        )
        val resourceName = "s$currentPictureId"
        val resId = drawableMap[resourceName] ?: 0
        viewHolder.image.setImageResource(resId)
    }
}