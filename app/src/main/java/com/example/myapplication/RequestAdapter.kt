package com.example.myapplication

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RequestAdapter(val context: Context, val requestList: ArrayList<Request>, val playerList: ArrayList<Player>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.request_layout, parent, false)
        return ReceiveViewHolder(view)

    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentRequest = requestList[position]
        val viewHolder = holder as ReceiveViewHolder
        var username: String? = null
        for(player in playerList) {
            if(currentRequest.senderId == player.uid) {
                username = player.username
                break
            }

        }
        viewHolder.receiveRequest.text = username + " sent you request!"
    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val receiveRequest = itemView.findViewById<TextView>(R.id.req)

        // ovde obraditi i Accept/Decline dugmad
    }
}