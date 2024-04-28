package com.example.myapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GameActivity : AppCompatActivity() {

    private lateinit var charactersRecyclerView: RecyclerView
    private lateinit var adapter: CharactersAdapter
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val opLabel: TextView = findViewById(R.id.op);
        opLabel.text = intent.getStringExtra("op")

        val image : ImageView = findViewById(R.id.card)
        //image.setImageResource(R.drawable.s38)

        var gameObjRef = intent.getStringExtra("gameObj")


        var mDbRef = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()
        var gameObj: Game = Game()

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
        mDbRef.child("game").child(gameObjRef!!)
            .get()
            .addOnSuccessListener { snapshot ->
                gameObj.player1 = snapshot.child("player1").value.toString()
                gameObj.player2 = snapshot.child("player2").value.toString()
                gameObj.cardsDown1 = snapshot.child("cardsDown1").value as Long
                gameObj.cardsDown2 = snapshot.child("cardsDown2").value as Long
                gameObj.player1image = snapshot.child("player1image").value as Long
                gameObj.player2image = snapshot.child("player2image").value as Long
                val player1card = gameObj.player1image
                val player2card = gameObj.player2image
                if(mAuth.currentUser!!.uid == gameObj.player1) {
                    var resourceName1 = "s$player1card"
                    var resId1 = drawableMap[resourceName1]
                    image.setImageResource(resId1!!)
                } else {
                    var resourceName2 = "s$player2card"
                    var resId2 = drawableMap[resourceName2]
                    image.setImageResource(resId2!!)
                }
                gameObj.finish = snapshot.child("finish").value as Long
                gameObj.imageIndices = snapshot.child("imageIndices").value as List<Long>
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error", exception)
            }

        charactersRecyclerView = findViewById(R.id.characters_list)
        charactersRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CharactersAdapter(this, gameObj.imageIndices)
        charactersRecyclerView.adapter = adapter
    }
}