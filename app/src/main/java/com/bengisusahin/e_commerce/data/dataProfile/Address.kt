package com.bengisusahin.e_commerce.data.dataProfile

data class Address(
    val address: String,
    val city: String,
    val coordinates: Coordinates?=null,
    val country: String,
    val postalCode: String?=null,
    val state: String,
    val stateCode: String?=null
)