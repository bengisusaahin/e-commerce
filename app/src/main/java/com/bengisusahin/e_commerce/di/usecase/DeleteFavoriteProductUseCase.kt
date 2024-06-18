package com.bengisusahin.e_commerce.di.usecase

import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.repository.FavoriteProductsRepository
import javax.inject.Inject

class DeleteFavoriteProductUseCase @Inject constructor(
    private val favoriteProductsRepository: FavoriteProductsRepository
){
    suspend operator fun invoke(favoriteProducts: FavoriteProducts) : Int{
        return favoriteProductsRepository.deleteFavoriteProduct(favoriteProducts)
    }
}