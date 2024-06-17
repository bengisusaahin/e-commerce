package com.bengisusahin.e_commerce.data.dataFavorites

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room database entity for favorite products
@Entity(tableName = "favorite_products")
data class FavoriteProducts(
    // Primary key for the favorite products
    @PrimaryKey(autoGenerate = true)
    val fid: Int,
    // user id from api for the favorite products of the user
    val uid: Long,
    // product id from api
    val pid: Int,
    val title: String,
    val price: Double,
    val image: String,
    val description: String
)
