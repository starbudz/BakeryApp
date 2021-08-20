package com.example.bakeryApp1.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import bakeryApp1.R
import com.example.bakeryapp1.welcome.SelectionFragment

class OnboardingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)

        //loading a default frag. for the frame layout
        supportFragmentManager.beginTransaction().replace(R.id.fragcontainer, SelectionFragment()).commit()

    }
}