package com.bengisusahin.e_commerce.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Insert
    suspend fun insertFavoriteProduct(favoriteProducts: FavoriteProducts) : Long

    @Query("SELECT * FROM favorite_products")
    fun getAllFavoriteProducts(): Flow<List<FavoriteProducts>>

    @Delete
    suspend fun deleteFavoriteProduct(favoriteProducts: FavoriteProducts) : Int

}