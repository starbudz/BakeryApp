package com.example.bakeryApp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import bakeryApp1.R
import com.example.bakeryApp1.welcome.OnboardingScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            //intents
                              val intent = Intent(this, OnboardingScreen::class.java)
                              startActivity(intent)
        }, 3000) //screen will show for 3 seconds
    }
}