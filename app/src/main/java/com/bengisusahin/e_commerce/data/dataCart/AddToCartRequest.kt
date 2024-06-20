package com.bengisusahin.e_commerce.data.dataCart

data class AddToCartRequest(
    val userId : Long,
    val products: List<AddToCartProduct>
)