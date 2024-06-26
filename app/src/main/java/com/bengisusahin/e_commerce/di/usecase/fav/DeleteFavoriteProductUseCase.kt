package com.bengisusahin.e_commerce.di.usecase.fav

import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.repository.FavoriteProductsRepository
import javax.inject.Inject

// This use case is used to delete a favorite product from the database
class DeleteFavoriteProductUseCase @Inject constructor(
    private val favoriteProductsRepository: FavoriteProductsRepository
){
    suspend operator fun invoke(favoriteProducts: FavoriteProducts) : Int{
        return favoriteProductsRepository.deleteFavoriteProduct(favoriteProducts)
    }
}