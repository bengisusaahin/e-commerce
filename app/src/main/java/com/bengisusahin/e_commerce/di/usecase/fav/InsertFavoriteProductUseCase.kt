package com.bengisusahin.e_commerce.di.usecase.fav

import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.repository.FavoriteProductsRepository
import javax.inject.Inject

// This class is used to insert a favorite product to the database
class InsertFavoriteProductUseCase @Inject constructor(
    private val favoriteProductsRepository: FavoriteProductsRepository
){
    suspend operator fun invoke(favoriteProducts: FavoriteProducts) : Long{
        return favoriteProductsRepository.insertFavoriteProduct(favoriteProducts)
    }
}