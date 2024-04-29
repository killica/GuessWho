package com.example.myapplication


import android.app.Activity
import android.content.ContentValues.TAG
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.DatabaseReference

class RequestAdapter(val context: Context, val requestList: ArrayList<Request>, val playerList: ArrayList<Player>, val keyList: ArrayList<String>,
                     val mDbRef: DatabaseReference):
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

        viewHolder.accept.setOnClickListener {
            acceptRequest(currentRequest)
        }

        viewHolder.decline.setOnClickListener {
            removeRequest(currentRequest)
        }
    }

    private fun acceptRequest(currentRequest: Request) {
        val position = requestList.indexOf(currentRequest)
        if(position != -1) {
            var opponentUsername: String? = null
            for(player in playerList) {
                if(currentRequest.senderId == player.uid) {
                    opponentUsername = player.username
                    break
                }
            }
            val gameObject = Game(currentRequest.senderId, currentRequest.receiverId)
            var gameRef = mDbRef.child("game").push()
            gameRef.setValue(gameObject)



            val intent = Intent(context, GameActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("op", "Opponent: " + opponentUsername)
            intent.putExtra("gameObj", gameRef.key)
            context.startActivity(intent)
            (context as Activity).finish()
            mDbRef.child("request").child(keyList[position]).removeValue()
            requestList.removeAt(position)
            notifyItemRemoved(position)

            // mi smo ovde receiver
            val dialogObject = Accept(currentRequest.receiverId, currentRequest.senderId, gameRef.key) // receiver-a -> sender-a
            mDbRef.child("accept").push().setValue(dialogObject)

        }
    }



    fun removeRequest(request: Request) {
        val position = requestList.indexOf(request)
        if (position != -1) {
            mDbRef.child("request").child(keyList[position]).removeValue()
            requestList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val receiveRequest = itemView.findViewById<TextView>(R.id.req)
        val accept = itemView.findViewById<Button>(R.id.accept)
        val decline = itemView.findViewById<Button>(R.id.decline)
    }
}