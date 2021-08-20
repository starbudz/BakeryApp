package com.example.bakeryApp1.admin.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import bakeryApp1.R
import com.example.bakeryApp1.admin.authenticate.LoginAdmin
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_admin_home.*
import java.io.IOException
import java.util.*

class AdminHomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var productName: String = ""
    lateinit var productImage: Uri
    var productPrice: String = ""
    var productDesc: String = ""
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var databaseReference: DatabaseReference? = null

    private val PICK_IMAGE_REQUEST = 71
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        //intialization of the products
        auth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().getReference("catalogueData")

        btnSignOut.setOnClickListener {
            auth.signOut()
            updateUi()
        }

        editProductImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )
        }

        btnUpload.setOnClickListener {
            productName = editProductName.text.toString()
            productPrice = editProductPrice.text.toString()
            productDesc = editProductDesc.text.toString()
            if (productDesc.trim() != "" && productName.trim() != "" && productPrice.trim() != "") {
                submitToFirebase(productPrice, productName, productDesc, productImage)
            }

        }

        btnView.setOnClickListener {
            val intent = Intent(this,DisplayActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            //checking if image was picked or not
            if (data == null || data.data == null) {
                return
            }
            productImage = data.data!!
            //showing user their selection
            //display image on imageview
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, productImage)
                //referencing imageview based off id : android extensions
                editProductImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun submitToFirebase(
        productPrice: String,
        productName: String,
        productDesc: String,
        productImage: Uri
    ) {

        //beginning processes to take data to firebase
        //UUID refers to the file name
        val ref = storageReference?.child("catalogueImages/" + UUID.randomUUID().toString())
        //put the file to storage
        val uploadTask = ref?.putFile(productImage!!)
        //monitoring the process
        val urlTask =
            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                //check exception if the process did not work
                if (!it.isSuccessful) {
                    it.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener {
                if (it.isSuccessful) {
                    //using the oncomplete listener from firebase we can now check if the upload is successful
                    //and then also pick the download uri : download address for the image saved
                    val downloadUri = it.result
                    //if upload is successful then we can now upload above uri to firebase real time database
                    Log.d("searchUrl", " download url is " + downloadUri.toString())
                    //taking details to firebase realtime database
                    //generating unique ids for records
                    val catalogueId  = databaseReference?.push()?.key
                    val catalogue = catalogueId.let { CatalogueModel(catalogueId.toString(),productName,productPrice,downloadUri.toString(),productDesc) }
                    //this will save our catalogue product to our firebase database
                    if (catalogueId != null) {
                        databaseReference?.child(catalogueId)?.setValue(catalogue)?.addOnCompleteListener {
                            Toast.makeText(
                                applicationContext,
                                "Catalogue added successfully",
                                Toast.LENGTH_LONG
                            ).show()

                        }?.addOnFailureListener {
                            Toast.makeText(
                                applicationContext, " Error occurred , check internet connection",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                } else {
                    // Handle failures //e.g //
                    Toast.makeText(
                        applicationContext, " Error occurred , check internet connection",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }?.addOnFailureListener {
                //here u can get actual error from firebase
                val messageError = it.message
                Toast.makeText(applicationContext, " Error is " + messageError, Toast.LENGTH_LONG)
                    .show()
            }


    }




    private fun updateUi() {
        val intent = Intent(applicationContext, LoginAdmin::class.java)
        startActivity(intent)
    }

}