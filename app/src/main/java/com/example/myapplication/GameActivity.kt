package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.ui.layout.Layout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginLeft
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GameActivity : AppCompatActivity() {

    private lateinit var charactersRecyclerView: RecyclerView
    private lateinit var adapter: CharactersAdapter
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val opLabel: TextView = findViewById(R.id.op);
        opLabel.text = intent.getStringExtra("op")

        var image: ImageView = findViewById(R.id.card)
        var text: TextView = findViewById(R.id.tv)
        var gameObjRef = intent.getStringExtra("gameObj")


        var mDbRef = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()
        var gameObj: Game = Game()

        //var scrollLayout: ScrollView = findViewById(R.id.scroll_view)
        //var gridLayout : GridLayout = findViewById(R.id.characters)

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
        val cardsMap = mapOf(
            "card0" to R.id.card0,
            "card1" to R.id.card1,
            "card2" to R.id.card2,
            "card3" to R.id.card3,
            "card4" to R.id.card4,
            "card5" to R.id.card5,
            "card6" to R.id.card6,
            "card7" to R.id.card7,
            "card8" to R.id.card8,
            "card9" to R.id.card9,
            "card10" to R.id.card10,
            "card11" to R.id.card11,
            "card12" to R.id.card12,
            "card13" to R.id.card13,
            "card14" to R.id.card14,
            "card15" to R.id.card15,
            "card16" to R.id.card16,
            "card17" to R.id.card17,
            "card18" to R.id.card18,
            "card19" to R.id.card19,
            "card20" to R.id.card20,
            "card21" to R.id.card21,
            "card22" to R.id.card22,
            "card23" to R.id.card23,
        )

        val pictureMap = mapOf(
            "picture0" to R.id.picture0,
            "picture1" to R.id.picture1,
            "picture2" to R.id.picture2,
            "picture3" to R.id.picture3,
            "picture4" to R.id.picture4,
            "picture5" to R.id.picture5,
            "picture6" to R.id.picture6,
            "picture7" to R.id.picture7,
            "picture8" to R.id.picture8,
            "picture9" to R.id.picture9,
            "picture10" to R.id.picture10,
            "picture11" to R.id.picture11,
            "picture12" to R.id.picture12,
            "picture13" to R.id.picture13,
            "picture14" to R.id.picture14,
            "picture15" to R.id.picture15,
            "picture16" to R.id.picture16,
            "picture17" to R.id.picture17,
            "picture18" to R.id.picture18,
            "picture19" to R.id.picture19,
            "picture20" to R.id.picture20,
            "picture21" to R.id.picture21,
            "picture22" to R.id.picture22,
            "picture23" to R.id.picture23,
        )

        val nameMap = mapOf(
            "name0" to R.id.name0,
            "name1" to R.id.name1,
            "name2" to R.id.name2,
            "name3" to R.id.name3,
            "name4" to R.id.name4,
            "name5" to R.id.name5,
            "name6" to R.id.name6,
            "name7" to R.id.name7,
            "name8" to R.id.name8,
            "name9" to R.id.name9,
            "name10" to R.id.name10,
            "name11" to R.id.name11,
            "name12" to R.id.name12,
            "name13" to R.id.name13,
            "name14" to R.id.name14,
            "name15" to R.id.name15,
            "name16" to R.id.name16,
            "name17" to R.id.name17,
            "name18" to R.id.name18,
            "name19" to R.id.name19,
            "name20" to R.id.name20,
            "name21" to R.id.name21,
            "name22" to R.id.name22,
            "name23" to R.id.name23,
        )
        val characterNamesMap = mapOf(
            "n0" to "Marjan",
            "n1" to "Marta",
            "n2" to "Petar",
            "n3" to "Andjelka",
            "n4" to "Kloi",
            "n5" to "Marko",
            "n6" to "Tijana",
            "n7" to "Stela",
            "n8" to "Glorija",
            "n9" to "Filip",
            "n10" to "Mila",
            "n11" to "Vanja",
            "n12" to "Kristofer",
            "n13" to "Noa",
            "n14" to "Pavle",
            "n15" to "Ilija",
            "n16" to "Sabrina",
            "n17" to "Mej",
            "n18" to "Dzekson",
            "n19" to "Ara",
            "n20" to "Aron",
            "n21" to "Teo",
            "n22" to "Stojadin",
            "n23" to "Mitar",
            "n24" to "Gavrilo",
            "n25" to "Lejla",
            "n26" to "Sheli",
            "n27" to "Teodora",
            "n28" to "Ana",
            "n29" to "Luk",
            "n30" to "Aiko",
            "n31" to "Dzastin",
            "n32" to "Brendon",
            "n33" to "Aleks",
            "n34" to "Nejtn",
            "n35" to "Milutin",
            "n36" to "Klara",
            "n37" to "Bojana",
            "n38" to "Miljana",
            "n39" to "Isak",
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
                if (mAuth.currentUser!!.uid == gameObj.player1) {
                    var resourceName1 = "s$player1card"
                    var characterName1 = "n$player1card"
                    var resId1 = drawableMap[resourceName1]
                    image.setImageResource(resId1!!)
                    text.text = characterNamesMap[characterName1]

                } else {
                    var resourceName2 = "s$player2card"
                    var characterName2 = "n$player2card"
                    var resId2 = drawableMap[resourceName2]
                    image.setImageResource(resId2!!)
                    text.text = characterNamesMap[characterName2]
                }
                gameObj.finish = snapshot.child("finish").value as Long
                mDbRef.child("game").child(gameObjRef).child("finish").addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d(TAG, "I IMAMO POBEDNIKA!" + snapshot.value.toString())
                        if(snapshot.value.toString() == "1" || snapshot.value.toString() == "2") {
                            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val popupView = inflater.inflate(R.layout.popup_layout, null)

                            val width = LinearLayout.LayoutParams.WRAP_CONTENT
                            val height = LinearLayout.LayoutParams.WRAP_CONTENT
                            val focusable = false
                            val popupWindow = PopupWindow(popupView, width, height, focusable)

                            popupWindow.animationStyle = R.style.PopupAnimation
                            val winnerInfo = popupView.findViewById<TextView>(R.id.info)
                            if(snapshot.value.toString() == "1") {
                                if (mAuth.currentUser!!.uid == gameObj.player1) {
                                    // mi smo pobedili
                                    winnerInfo.text = "Congratulations! You won!"
                                } else {
                                    //mi smo izgubili
                                    winnerInfo.text = "Better luck next time :("
                                }
                            } else {
                                if (mAuth.currentUser!!.uid == gameObj.player2) {
                                    // mi smo pobedili
                                    winnerInfo.text = "Congratulations! You won!"
                                } else {
                                    //mi smo izgubili
                                    winnerInfo.text = "Better luck next time :("
                                }
                            }
                            val backButton = popupView.findViewById<Button>(R.id.end_game)
                            backButton.setOnClickListener {
                                popupWindow.dismiss()
                                mDbRef.child("game").child(gameObjRef).removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val intent = Intent(this@GameActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    } else {

                                    }
                                }


                            }
                            popupWindow.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, "Nista")
                    }

                })



                gameObj.imageIndices = snapshot.child("imageIndices").value as List<Long>
                val flipped = BooleanArray(24)
                for(i in 0 until 24) {
                    val cardKey = "card$i"
                    val pictureKey = "picture$i"
                    val nameKey = "name$i"
                    val ind = gameObj.imageIndices.get(i)
                    val resKey = "s$ind"
                    val characterNameKey = "n$ind"
                    var cardView : CardView = findViewById(cardsMap[cardKey]!!)
                    var pictureView: ImageView = findViewById(pictureMap[pictureKey]!!)
                    pictureView.setImageResource(drawableMap[resKey]!!)
                    var textView : TextView = findViewById(nameMap[nameKey]!!)
                    var chName : String? = characterNamesMap[characterNameKey]
                    textView.text = chName
                    textView.setTextColor(Color.DKGRAY)

                    cardView.setOnClickListener {
                        //Toast.makeText(this@GameActivity, "CardView" + i + "Clicked", Toast.LENGTH_SHORT).show()
                        flipped[i] = true
                        pictureView.setImageResource(R.drawable.logo)
                        val spannableString = SpannableString(chName)
                        spannableString.setSpan(StrikethroughSpan(), 0, chName!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        textView.text = spannableString
                        textView.setTextColor(Color.rgb(255, 215, 0))
                        cardView.setOnClickListener(null)
                        //cardView.setBackgroundResource(R.drawable.back)

                        if (mAuth.currentUser!!.uid == gameObj.player1) {
                            // prvom igracu povecaj ili smanji
                            gameObj.cardsDown1 = gameObj.cardsDown1 + 1
                            if(gameObj.cardsDown1 == 23L) {
                                for(j in 0 until 24) {
                                    if(!flipped[j]) {
                                        val ind1 = gameObj.imageIndices.get(j)
                                        val characterNameKey1 = "n$ind1"
                                        val lastName = characterNamesMap[characterNameKey1]

                                        val opponentCard = gameObj.player2image
                                        val opponentCardName = "n$opponentCard"
                                        val opponentCharacterName = characterNamesMap[opponentCardName]

                                        if(lastName == opponentCharacterName) {
                                            gameObj.finish = 1
                                        } else {
                                            gameObj.finish = 2
                                        }
                                        mDbRef.child("game").child(gameObjRef).child("finish").setValue(gameObj.finish)
                                            .addOnSuccessListener {
                                                Log.d(TAG, "Uspesno azurirana vrednost FINISHA")
                                            }
                                            .addOnFailureListener {
                                                Log.d(TAG, "Neuspesno azurirana vrednost FINISHA")
                                            }
                                    }
                                }
                            }
                            mDbRef.child("game").child(gameObjRef).child("cardsDown1").setValue(gameObj.cardsDown1)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Uspesno azurirana vrednost")
                                }
                                .addOnFailureListener {
                                    Log.d(TAG, "Neuspesno azurirana vrednost")
                                }

                        } else {
                            // drugom igracu povecaj ili smanji
                            gameObj.cardsDown2 = gameObj.cardsDown2 + 1
                            if(gameObj.cardsDown2 == 23L) {
                                for(j in 0 until 24) {
                                    if(!flipped[j]) {
                                        val ind1 = gameObj.imageIndices.get(j)
                                        val characterNameKey1 = "n$ind1"
                                        val lastName = characterNamesMap[characterNameKey1]

                                        val opponentCard = gameObj.player1image
                                        val opponentCardName = "n$opponentCard"
                                        val opponentCharacterName = characterNamesMap[opponentCardName]

                                        if(lastName == opponentCharacterName) {
                                            gameObj.finish = 2
                                        } else {
                                            gameObj.finish = 1
                                        }
                                        mDbRef.child("game").child(gameObjRef).child("finish").setValue(gameObj.finish)
                                            .addOnSuccessListener {
                                                Log.d(TAG, "Uspesno azurirana vrednost FINISHA")
                                            }
                                            .addOnFailureListener {
                                                Log.d(TAG, "Neuspesno azurirana vrednost FINISHA")
                                            }

                                    }
                                }
                            }
                            mDbRef.child("game").child(gameObjRef).child("cardsDown2").setValue(gameObj.cardsDown2)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Uspesno azurirana vrednost")
                                }
                                .addOnFailureListener {
                                    Log.d(TAG, "Neuspesno azurirana vrednost")
                                }
                        }
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error", exception)
            }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }

}
