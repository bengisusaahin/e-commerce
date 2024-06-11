package com.bengisusahin.e_commerce.data

data class User(
    val name: String,
    val email: String
){
    constructor():this("","" )
}
