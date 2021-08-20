package com.example.bakeryApp1.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import bakeryApp1.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bakeryApp1.admin.home.CatalogueAdapter
import com.example.bakeryApp1.admin.home.CatalogueModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display.*

class HomeActivity : AppCompatActivity() {

    private var databaseReference: DatabaseReference? = null
    lateinit var recycler: RecyclerView
    //declaring a mutable list to store my list of catalogue product
    lateinit var catList: MutableList<HomeModel>
    lateinit var HomeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //intializing ref to  firebase db to pull data from
        databaseReference = FirebaseDatabase.getInstance().getReference("catalogueData")
        //my heroes list
        catList = mutableListOf()

        //read our data from firebase
        databaseReference!!.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.

                //here checking if data exists in firebase node using the snapshot tag of the DataSnapshot which saves the records
                if(snapshot!!.exists()) {
                    //updating our mutable list everytime a change is effected on node
                    catList.clear()
                    //using for loop to iterate over the records in the node
                    for (h in snapshot.children) {
                        //adding records to my model class
                        val bal = h.getValue(HomeModel::class.java)
                        //adding details to my mutable list
                        catList?.add(bal!!)
                    }

                    //tagging my adapter for communication between my data class and this activity
                    val adapter = HomeAdapter(this@HomeActivity!!, catList)
                    //setting the adapter for the recyclerView
                    recyclerProducts?.setAdapter(adapter)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"Something went wrong, try again, Check internet connection" +  error.details, Toast.LENGTH_LONG).show()
                Log.d("error","error is " + error.details)
            }

        })

        //setting the layout orientation for my recycler view
        recyclerProducts.layoutManager = LinearLayoutManager(this)
        //setting size to data size
        recyclerProducts.setHasFixedSize(true)


    }
}
