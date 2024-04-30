package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
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
        var passwordVisible = false
        edtPassword.setOnTouchListener(object: OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val Right = 2
                if(event!!.action == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= edtPassword.right - edtPassword.compoundDrawables[Right].bounds.width()) {
                        var selection = edtPassword.selectionEnd
                        if(passwordVisible) {
                            edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_key_24, 0)
                            edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                            passwordVisible = false
                        } else {
                            edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_key_off_24, 0)
                            edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                            passwordVisible = true
                        }
                        edtPassword.setSelection(selection)
                        return true
                    }
                }

                return false
            }
        })


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