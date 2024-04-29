package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUp : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()

        edtUsername = findViewById(R.id.username)
        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.password)
        btnRegister = findViewById(R.id.register)

        btnRegister.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()

            signUp(username, email, password)

        }

    }

    private fun signUp(username: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addPlayerToDatabase(username, email, mAuth.currentUser?.uid!!);
                    val intent = Intent(this@SignUp, Login::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUp, "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addPlayerToDatabase(username: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val usersRef = FirebaseDatabase.getInstance().getReference("users")

        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(this@SignUp, "Username not available!", Toast.LENGTH_SHORT).show()
                } else {
                    mDbRef.child("player").child(uid).setValue(Player(username, email, uid))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

}