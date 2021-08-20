package com.example.bakeryApp1.admin.home

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import bakeryApp1.R
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class CatalogueAdapter(private val context: Context, private val catalogue: List<CatalogueModel>) :
    RecyclerView.Adapter<CatalogueAdapter.CatalogueViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogueAdapter.CatalogueViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_item, parent, false)
        return CatalogueViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CatalogueAdapter.CatalogueViewHolder, position: Int) {
        //current position
        val currentProduct = catalogue[position]
        //bind data from the model to adapter
        holder.productName.text = currentProduct.productName
        holder.productPrice.text = currentProduct.productPrice
        holder.productDesc.text = currentProduct.productDesc
        Glide.with(context)
            .load(currentProduct.productImage)
            .into(holder.productImage)

        val price = currentProduct.productPrice
        val id = currentProduct.id
        val desc = currentProduct.productDesc
        val name = currentProduct.productName
        val imagePath = currentProduct.productImage

        holder.itemClick.setOnClickListener {
            updateandDeleteDialog(price,id,desc,name,imagePath)
        }
    }

    private fun updateandDeleteDialog(
        price: String,
        id: String,
        desc: String,
        name: String,
        imagePath: String
    ) {
           //raise my dialog
        //raising a dialog
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //attaching the dialog interface
        val dialogView = inflater.inflate(R.layout.updateanddeletedialog, null)
        dialogBuilder.setView(dialogView)
        //view identification
        val updatePrice = dialogView.findViewById<TextInputEditText>(R.id.SetQuantity)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)
        val btnUpdate = dialogView.findViewById<Button>(R.id.btnUpdate)

        //customize the dialog box
        dialogBuilder.setTitle("Update or Delete a Product")
        dialogBuilder.setIcon(R.drawable.icon)
        //create and show
        val dialog = dialogBuilder.create()
        dialog.show()
        //set on click methods
        btnUpdate.setOnClickListener {
            val priceEntry = updatePrice.text.toString()
            updateProductFirebase(priceEntry,id,name,desc,imagePath)
            dialog.dismiss()
            Toast.makeText(context,"Update success",Toast.LENGTH_LONG).show()
        }

        btnDelete.setOnClickListener {
            deleteProductFirebase(id)
            dialog.dismiss()
            Toast.makeText(context,"Delete success",Toast.LENGTH_LONG).show()
        }



    }

    private fun deleteProductFirebase(id: String) {
        //create ref to db
        val databaseReference = FirebaseDatabase.getInstance().getReference("catalogueData").child(id)
        databaseReference.removeValue()
    }

    private fun updateProductFirebase(
        priceEntry: String,
        id: String,
        name: String,
        desc: String,
        imagePath: String
    ) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("catalogueData").child(id)
        //taking data to model
        val catalogue = CatalogueModel(id,name,priceEntry,imagePath,desc)
        databaseReference.setValue(catalogue)
    }

    override fun getItemCount() = catalogue.size


   class CatalogueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
          var productImage = itemView.findViewById<ImageView>(R.id.image_view)!!
          var productName = itemView.findViewById<TextView>(R.id.text_view_1)!!
          var productPrice = itemView.findViewById<TextView>(R.id.text_view_2)!!
          var productDesc = itemView.findViewById<TextView>(R.id.text_view_3)!!
          var itemClick = itemView.findViewById<CardView>(R.id.cardClick)!!
   }

}