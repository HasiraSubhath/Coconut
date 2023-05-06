package com.example.kotlin_bill.models

data class OrderModel(
    var productId: String? = null,
    var productName: String? = null,
    var productQty: String? = null,
    var productDate: String? = null,
    var productPrice: String? = null
    
)