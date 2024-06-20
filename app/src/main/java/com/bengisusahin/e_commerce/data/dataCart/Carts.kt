package com.bengisusahin.e_commerce.data.dataCart

data class Carts(
    val carts: List<Cart>,
    val limit: Int,
    val skip: Int,
    val total: Int
)