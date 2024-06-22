package com.bengisusahin.e_commerce.data.dataProduct

data class Review(
    val comment: String,
    val date: String,
    val rating: Int,
    val reviewerEmail: String,
    val reviewerName: String
)