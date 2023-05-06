package com.example.kotlin_bill.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kotlin_bill.models.OrderModel
import com.example.kotlin_bill.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrderInsertionActivity : AppCompatActivity() {
    //initializing variables

    private lateinit var etProductName: EditText
    private lateinit var etProductQty: EditText
    private lateinit var etProductDate: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_insertion)

        etProductName = findViewById(R.id.etProductName)
        etProductQty = findViewById(R.id.etProductQty)
        etProductDate = findViewById(R.id.etProductDate)
        etProductPrice = findViewById(R.id.etProductPrice)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("CoconutDB")

        btnSaveData.setOnClickListener {
            saveCardData()
        }

    }

    private fun saveCardData() {

        //Geting Values
        val productName = etProductName.text.toString()
        val productQty = etProductQty.text.toString()
        val productDate = etProductDate.text.toString()
        val productPrice = etProductPrice.text.toString()

        //validation
        if(productName.isEmpty() || productQty.isEmpty() || productDate.isEmpty() || productPrice.isEmpty()){
        if (productName.isEmpty()) {
            etProductName.error = "Please enter Product Name"
        }
        if (productQty.isEmpty()) {
            etProductQty.error = "Please enter quantity"
        }
        if (productDate.isEmpty()) {
            etProductDate.error = "Please enter  Date"
        }
        if (productPrice.isEmpty()) {
            etProductPrice.error = "Please enter price"
        }
            Toast.makeText(this, "Some areas are not filled", Toast.LENGTH_LONG).show()
        }
        else{
        //genrate unique ID
        val productId = dbRef.push().key!!

        val card = OrderModel(productId, productName, productQty, productDate, productPrice)

        dbRef.child(productId).setValue(card)
            .addOnCompleteListener {
                Toast.makeText(this,"data insert successfully",Toast.LENGTH_SHORT).show()

                //clear data after insert
                etProductName.text.clear()
                etProductQty.text.clear()
                etProductDate.text.clear()
                etProductPrice.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()
            }

    }

}}