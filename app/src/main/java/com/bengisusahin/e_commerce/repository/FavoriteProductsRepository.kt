package com.bengisusahin.e_commerce.repository

import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.database.FavoriteProductDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Repository class for favorite products that handles database operations
class FavoriteProductsRepository @Inject constructor(
    private val favoriteProductDao: FavoriteProductDao
){
    suspend fun insertFavoriteProduct(favoriteProducts: FavoriteProducts) : Long{
        return favoriteProductDao.insertFavoriteProduct(favoriteProducts)
    }
    suspend fun deleteFavoriteProduct(favoriteProducts: FavoriteProducts) : Int{
        return favoriteProductDao.deleteFavoriteProduct(favoriteProducts)
    }
    fun getAllFavoriteProducts(userId: Long) : Flow<List<FavoriteProducts>> {
        return favoriteProductDao.getAllFavoriteProducts(userId)
    }
    suspend fun isFavorite(userId: Long, productId: Int) : Boolean{
        return favoriteProductDao.isFavorite(userId, productId) > 0
    }
    suspend fun getFid(userId: Long, productId: Int): Int?{
        return favoriteProductDao.getFid(userId, productId)
    }
}