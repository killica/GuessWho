package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(val context: Context, val playerList: ArrayList<Player>):
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.player_layout, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val currentPlayer = playerList[position]

        holder.textName.text = currentPlayer.username
    }

    class PlayerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.tv1)
    }
}