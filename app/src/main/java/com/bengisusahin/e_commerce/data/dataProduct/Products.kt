package com.bengisusahin.e_commerce.data.dataProduct

data class Products(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)