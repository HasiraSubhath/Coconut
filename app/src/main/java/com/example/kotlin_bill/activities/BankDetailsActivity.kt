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
import com.example.kotlin_bill.models.BankModel
import com.google.firebase.database.FirebaseDatabase

class BankDetailsActivity : AppCompatActivity() {

    private lateinit var tvBankId: TextView
    private lateinit var tvBankName: TextView
    private lateinit var tvBankBranch: TextView
    private lateinit var tvBankAmount: TextView
    private lateinit var tvCardCvv: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("bankId").toString(),
                intent.getStringExtra("bankName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("bankId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Card data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BankFetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvBankId = findViewById(R.id.tvBankId)
        tvBankName = findViewById(R.id.tvBankName)
        tvBankBranch = findViewById(R.id.tvBankBranch)
        tvBankAmount = findViewById(R.id.tvBankAmount)
        tvCardCvv = findViewById(R.id.tvCardCvv)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvBankId.text = intent.getStringExtra("bankId")
        tvBankName.text = intent.getStringExtra("bankName")
        tvBankBranch.text = intent.getStringExtra("bankBranch")
        tvBankAmount.text = intent.getStringExtra("bankAmount")
        tvCardCvv.text = intent.getStringExtra("cardCvv")

    }

    private fun openUpdateDialog(
        bankId: String,
        bankName: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.bank_update_dialog, null)

        mDialog.setView(mDialogView)

        val etBankName = mDialogView.findViewById<EditText>(R.id.etBankName)
        val etBankBranch = mDialogView.findViewById<EditText>(R.id.etBankBranch)
        val etBankAmount = mDialogView.findViewById<EditText>(R.id.etBankAmount)
        val etCardCvv = mDialogView.findViewById<EditText>(R.id.etCardCvv)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etBankName.setText(intent.getStringExtra("bankName").toString())
        etBankBranch.setText(intent.getStringExtra("bankBranch").toString())
        etBankAmount.setText(intent.getStringExtra("bankAmount").toString())
        etCardCvv.setText(intent.getStringExtra("cardCvv").toString())

        mDialog.setTitle("Updating $bankName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                bankId,
                etBankName.text.toString(),
                etBankBranch.text.toString(),
                etBankAmount.text.toString(),
                etCardCvv.text.toString()
            )

            Toast.makeText(applicationContext, "Card Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvBankName.text = etBankName.text.toString()
            tvBankBranch.text = etBankBranch.text.toString()
            tvBankAmount.text = etBankAmount.text.toString()
            tvCardCvv.text = etCardCvv.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String,
        cvv: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("PaymentDB").child(id)
        val empInfo = BankModel(id, name, age, salary, cvv)
        dbRef.setValue(empInfo)
    }
}