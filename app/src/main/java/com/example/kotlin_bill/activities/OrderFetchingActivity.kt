package com.example.kotlin_bill.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_bill.R
import com.example.kotlin_bill.adapters.OrderAdapter
import com.example.kotlin_bill.models.OrderModel
import com.google.firebase.database.*

class OrderFetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<OrderModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_fetching)

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<OrderModel>()

        getCardData()


    }

    private fun getCardData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("CoconutDB")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(OrderModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = OrderAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : OrderAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@OrderFetchingActivity, OrderDetailsActivity::class.java)

                            //put extra(passing data to another activity)
                            intent.putExtra("productId", empList[position].productId)
                            intent.putExtra("productName", empList[position].productName)
                            intent.putExtra("productQty", empList[position].productQty)
                            intent.putExtra("productDate", empList[position].productDate)
                            intent.putExtra("productPrice", empList[position].productPrice)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}