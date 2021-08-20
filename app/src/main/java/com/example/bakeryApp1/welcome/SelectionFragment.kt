package com.example.bakeryapp1.welcome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.bakeryApp1.admin.authenticate.LoginAdmin
import com.example.bakeryApp1.users.HomeActivity
import bakeryApp1.R


class SelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_selection, container, false)
        val home = view.findViewById<Button>(R.id.btnUser)
        val admin = view.findViewById<Button>(R.id.btnAdmin)

        home.setOnClickListener {
            //moving from activity to fragment
            val intent = Intent(activity, HomeActivity::class.java)
            activity?.startActivity(intent)
        }

        admin.setOnClickListener {
            val intent = Intent(activity, LoginAdmin::class.java)
            activity?.startActivity(intent)
        }
        return view
    }
}