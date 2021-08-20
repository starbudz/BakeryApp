package com.example.bakeryApp1.admin.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import bakeryApp1.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var emailReset: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        tvLogin.setOnClickListener {
            val intent = Intent(applicationContext,LoginAdmin::class.java)
            startActivity(intent)
        }

        btnForgot.setOnClickListener {
            emailReset = editEmail.text.toString()
            if (emailReset.isEmpty()){
                Toast.makeText(applicationContext,"Email should not be empty", Toast.LENGTH_LONG).show()

            } else {
                resetFirebase(emailReset)
            }
        }
    }

    private fun updateUi() {
        val intent = Intent(applicationContext, LoginAdmin::class.java)
        startActivity(intent)
    }

    private fun resetFirebase(emailReset: String) {
          auth.sendPasswordResetEmail(emailReset)
              .addOnCompleteListener {
                   if (it.isSuccessful){
                       Toast.makeText(applicationContext,"Reset link sent to email,check inbox to reset",Toast.LENGTH_LONG).show()
                       Log.d("auth","details are " + it.result)
                       updateUi()
                   } else {
                       Toast.makeText(applicationContext,"something went wrong try again",Toast.LENGTH_LONG).show()
                       Log.d("auth","details are " + it.exception)
                   }
              }
    }
}
















