package com.example.bakeryApp1.admin.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import bakeryApp1.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register_admin.*

class RegisterAdmin : AppCompatActivity() {
    var username: String = ""
    var email: String = ""
    var conpass: String = ""
    var pass: String = ""
    var confirmedPassword: String = ""
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_admin)
        //getting instance of firebase auth product
        auth = FirebaseAuth.getInstance()

        tvLogin.setOnClickListener{
            val intent = Intent(applicationContext, LoginAdmin::class.java)
            startActivity(intent)
        }
        tvForgot.setOnClickListener{
            val intent = Intent(applicationContext, ForgotPassword::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            captureInput()
        }
    }

    private fun captureInput() {
        username = editUsername.text.toString()
        email = editEmail.text.toString()
        pass = editPassword.text.toString()
        conpass = editConPass.text.toString()

        if (pass.equals(conpass)){
            confirmedPassword = pass
        } else {
            Toast.makeText(applicationContext,"Passwords do not match",Toast.LENGTH_LONG).show()
        }

        registerToFirebase(email,confirmedPassword)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUi();
        }
    }

    private fun updateUi() {
        val intent = Intent(applicationContext, LoginAdmin::class.java)
        startActivity(intent)
    }

    private fun registerToFirebase(email: String, confirmedPassword: String) {
        auth.createUserWithEmailAndPassword(email,confirmedPassword)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful){
                         Toast.makeText(applicationContext,"Account created",Toast.LENGTH_LONG).show()
                         Log.d("auth","details are " + it.result)
                         updateUi()
                    } else {
                        Toast.makeText(applicationContext,"Account not created, try again",Toast.LENGTH_LONG).show()
                        Log.d("auth","details are " + it.exception)
                    }
                }
    }
}














