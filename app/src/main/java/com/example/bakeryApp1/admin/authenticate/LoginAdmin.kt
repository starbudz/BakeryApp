package com.example.bakeryApp1.admin.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import bakeryApp1.R
import com.example.bakeryApp1.admin.home.AdminHomeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_admin.*


class LoginAdmin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var emailLogin: String = ""
    var passLogin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        auth = FirebaseAuth.getInstance()


        tvSignUp.setOnClickListener{
            val intent = Intent(applicationContext, RegisterAdmin::class.java)
            startActivity(intent)
        }
        tvForgot.setOnClickListener{
            val intent = Intent(applicationContext, ForgotPassword::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            captureInput()
        }
    }

    private fun captureInput() {
        emailLogin = editEmail.text.toString()
        passLogin = editPassword.text.toString()
        if (emailLogin.isEmpty() && passLogin.isEmpty()){
            Toast.makeText(applicationContext,"Fields cannot be empty", Toast.LENGTH_LONG).show()

        } else {
             loginUser(emailLogin,passLogin)
        }
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
        val intent = Intent(applicationContext, AdminHomeActivity::class.java)
        startActivity(intent)
    }
    private fun loginUser(emailLogin: String, passLogin: String) {
           auth.signInWithEmailAndPassword(emailLogin,passLogin)
               .addOnCompleteListener {
                     if (it.isSuccessful){
                         Toast.makeText(applicationContext,"Login Verified",Toast.LENGTH_LONG).show()
                         Log.d("auth","details are " + it.result)
                         updateUi()
                     } else {
                         Toast.makeText(applicationContext,"Login Failed, try again",Toast.LENGTH_LONG).show()
                         Log.d("auth","details are " + it.exception)
                     }
               }
    }
}


















