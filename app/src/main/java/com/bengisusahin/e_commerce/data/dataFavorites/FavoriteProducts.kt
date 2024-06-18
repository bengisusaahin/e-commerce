package com.bengisusahin.e_commerce.data.dataFavorites

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
// Room database entity for favorite products
// uid and pid are used as a composite primary key to prevent duplicate entries
@Entity(tableName = "favorite_products", indices = [Index(value = ["uid", "pid"], unique = true)])
data class FavoriteProducts(
    @PrimaryKey(autoGenerate = true)
    // Primary key for the favorite products
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