package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var rulesTv : TextView
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.email)
        edtPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.login)
        btnSignup = findViewById(R.id.signup)
        rulesTv = findViewById(R.id.rules)
        var passwordVisible = false
        edtPassword.setOnTouchListener(object: View.OnTouchListener {
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

        btnSignup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        rulesTv.setOnClickListener {
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()

            if(email == "") {
                Toast.makeText(this@Login, "Email field empty", Toast.LENGTH_SHORT).show()
            } else if (password == "") {
                Toast.makeText(this@Login, "Password field empty", Toast.LENGTH_SHORT).show()
            } else {
                login(email, password);
                edtEmail.setText("")
                edtPassword.setText("")
            }
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login , MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@Login, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
    }
}