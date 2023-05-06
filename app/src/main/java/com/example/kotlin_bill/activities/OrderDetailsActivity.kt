package com.example.kotlin_bill.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kotlin_bill.R
import com.example.kotlin_bill.models.OrderModel
import com.google.firebase.database.FirebaseDatabase

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var tvProductId: TextView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductQty: TextView
    private lateinit var tvProductDate: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("productId").toString(),
                intent.getStringExtra("productName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("productId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("CoconutDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Order data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, OrderFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvProductId = findViewById(R.id.tvProductId)
        tvProductName = findViewById(R.id.tvProductName)
        tvProductQty = findViewById(R.id.tvProductQty)
        tvProductDate = findViewById(R.id.tvProductDate)
        tvProductPrice = findViewById(R.id.tvProductPrice)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvProductId.text = intent.getStringExtra("productId")
        tvProductName.text = intent.getStringExtra("productName")
        tvProductQty.text = intent.getStringExtra("productQty")
        tvProductDate.text = intent.getStringExtra("productDate")
        tvProductPrice.text = intent.getStringExtra("productPrice")

    }

    private fun openUpdateDialog(
        productId: String,
        productName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.order_update_dialog, null)

        mDialog.setView(mDialogView)

        val etProductName = mDialogView.findViewById<EditText>(R.id.etProductName)
        val etProductQty = mDialogView.findViewById<EditText>(R.id.etProductQty)
        val etProductDate = mDialogView.findViewById<EditText>(R.id.etProductDate)
        val etProductPrice = mDialogView.findViewById<EditText>(R.id.etProductPrice)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etProductName.setText(intent.getStringExtra("productName").toString())
        etProductQty.setText(intent.getStringExtra("productQty").toString())
        etProductDate.setText(intent.getStringExtra("productDate").toString())
        etProductPrice.setText(intent.getStringExtra("productPrice").toString())

        mDialog.setTitle("Updating $productName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateCardData(
                productId,
                etProductName.text.toString(),
                etProductQty.text.toString(),
                etProductDate.text.toString(),
                etProductPrice.text.toString()
            )

            Toast.makeText(applicationContext, "Order Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvProductName.text = etProductName.text.toString()
            tvProductQty.text = etProductQty.text.toString()
            tvProductDate.text = etProductDate.text.toString()
            tvProductPrice.text = etProductPrice.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateCardData(
        id: String,
        name: String,
        age: String,
        salary: String,
        cvv: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("CoconutDB").child(id)
        val cardInfo = OrderModel(id, name, age, salary, cvv)
        dbRef.setValue(cardInfo)
    }
}