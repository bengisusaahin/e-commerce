package com.bengisusahin.e_commerce.repository

import android.content.Context
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.database.FavoriteProductDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteProductsRepository @Inject constructor(
    private val favoriteProductDao: FavoriteProductDao
){
    suspend fun insertFavoriteProduct(favoriteProducts: FavoriteProducts) {
        favoriteProductDao.insertFavoriteProduct(favoriteProducts)
    }
    suspend fun deleteFavoriteProduct(favoriteProducts: FavoriteProducts) {
        favoriteProductDao.deleteFavoriteProduct(favoriteProducts)
    }
    fun getAllFavoriteProducts() : Flow<List<FavoriteProducts>> {
        return favoriteProductDao.getAllFavoriteProducts()
    }
}