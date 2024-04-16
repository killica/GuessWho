package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var playerWelcome: TextView
    //private lateinit var playerRecyclerView: RecyclerView
    private lateinit var playerList: ArrayList<Player>
    //private lateinit var adapter: PlayerAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        playerList = ArrayList()
        //adapter = PlayerAdapter(this, playerList)

        playerWelcome = findViewById(R.id.welcome)

        spinner = findViewById(R.id.spinner)

        val adapterS = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            mutableListOf("Select your opponent")
        )
        adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterS
        spinner.prompt = "Select your opponent"

        //playerRecyclerView = findViewById(R.id.playerRecyclerView)
        //playerRecyclerView.layoutManager = LinearLayoutManager(this)
        //playerRecyclerView.adapter = adapter



        mDbRef.child("player").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                playerList.clear()
                var cp: Player = Player()
                for(postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(Player::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid) {
                        playerList.add(currentUser!!)
                        adapterS.add(currentUser!!.username)
                    }
                    else
                        cp = currentUser!!
                }

                playerWelcome.text = "Welcome, " +  cp.username + "!\n"
                //adapter.notifyDataSetChanged()
                adapterS.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}