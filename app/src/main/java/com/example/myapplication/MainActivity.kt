package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var playerWelcome: TextView
    private lateinit var requestBtn: Button
    private lateinit var requestRecyclerView: RecyclerView
    private lateinit var playerList: ArrayList<Player>
    private lateinit var requestList: ArrayList<Request>
    private lateinit var keyList: ArrayList<String>
    private lateinit var adapter: RequestAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var logoutButton: Button

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
        playerWelcome = findViewById(R.id.welcome)

        requestBtn = findViewById(R.id.requestBtn)

        requestList = ArrayList()
        keyList = ArrayList()
        requestRecyclerView = findViewById(R.id.request_list)

        requestRecyclerView.layoutManager = LinearLayoutManager(this)


        adapter = RequestAdapter(this, requestList, playerList, keyList, mDbRef)
        requestRecyclerView.adapter = adapter

        spinner = findViewById(R.id.spinner)

        val adapterS = ArrayAdapter<String>(
            this,
            R.layout.simple_spinner_item,
            mutableListOf("Select your opponent")
        )
        adapterS.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterS
        spinner.prompt = "Select your opponent"

        logoutButton = findViewById(R.id.logout)
        logoutButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }


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

        var selectedValue: String? = null
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedValue = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // prijaviti gresku da mora da se selektuje prvo u spinneru
                Toast.makeText(this@MainActivity, "Opponent not selected!", Toast.LENGTH_SHORT).show()
            }

        }
        mDbRef.child("request").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                requestList.clear()
                keyList.clear()
                for(postSnapshot in snapshot.children) {
                    val request = postSnapshot.getValue(Request::class.java)
                    val key = postSnapshot.key
                    if (mAuth.currentUser?.uid == request?.receiverId) {
                        requestList.add(request!!)
                        keyList.add(key!!)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        mDbRef.child("accept").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // pretrazujemo da li je nama neko prihvatio zahtev za igru i prikazujemo iskacuci prozor ako jeste
                for(postSnapshot in snapshot.children) {
                    val accept = postSnapshot.getValue(Accept::class.java)
                    if (mAuth.currentUser?.uid == accept?.receiverId) {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        var accepterUsername: String? = null
                        for(player in playerList) {
                            if(accept!!.senderId == player.uid) {
                                accepterUsername = player.username
                                break
                            }
                        }
                        builder.setTitle("Challenge accepted!")
                        builder.setMessage(accepterUsername + " accepted your request!")
                        builder.setPositiveButton("Accept") { dialog, which ->
                            // treba izbrisati ovaj objekat iz baze
                            mDbRef.child("accept").child(postSnapshot.key!!).removeValue()
                            val intent = Intent(this@MainActivity, GameActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("op", "Opponent: " + accepterUsername)
                            intent.putExtra("gameObj", accept!!.gameRef)
                            startActivity(intent)
                            finish()
                        }
                        val dialog = builder.create()
                        if (!this@MainActivity.isFinishing) {
                            dialog.show()
                        }


                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val handler = Handler()
        requestBtn.setOnClickListener{
            if(selectedValue == "Select your opponent") {
                Toast.makeText(this@MainActivity, "Opponent not selected!", Toast.LENGTH_SHORT).show()
            } else {

                var found = false
                val senderId = FirebaseAuth.getInstance().currentUser?.uid
                var receiverId : String? = null
                for (player in playerList) {
                    if (selectedValue == player.username) {
                        receiverId = player.uid
                        break
                    }
                }

                mDbRef.child("request").addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(postSnapshot in snapshot.children) {
                            val req = postSnapshot.getValue(Request::class.java)
                            if(req!!.receiverId == receiverId && req.senderId == senderId && !found) {
                                Toast.makeText(this@MainActivity, "Request already sent!", Toast.LENGTH_SHORT).show()
                                found = true
                                break
                            }
                        }
                        if(!found) {
                            found = true
                            requestBtn.isEnabled = false
                            requestBtn.setBackgroundResource(R.drawable.btn2_qt)
                            handler.postDelayed({
                                requestBtn.setBackgroundResource(R.drawable.btn_qt)
                                requestBtn.isEnabled = true
                            }, 5000)
                            val requestObject = Request(senderId, receiverId)
                            mDbRef.child("request").push().setValue(requestObject)
                            Toast.makeText(this@MainActivity, "Request successfully sent!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

        }


    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}