package com.bengisusahin.e_commerce.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    // return type for the show warning to user
    // on conflict strategy is used to ignore the conflict
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteProduct(favoriteProducts: FavoriteProducts) : Long

    @Query("SELECT * FROM favorite_products WHERE uid = :userId")
    fun getAllFavoriteProducts(userId: Long): Flow<List<FavoriteProducts>>

    @Delete
    suspend fun deleteFavoriteProduct(favoriteProducts: FavoriteProducts) : Int

    @Query("SELECT count(*) FROM favorite_products WHERE pid = :productId")
    suspend fun isFavorite(productId: Int) : Int
}