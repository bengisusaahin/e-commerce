package com.bengisusahin.e_commerce.di.usecase.fav

import com.bengisusahin.e_commerce.repository.FavoriteProductsRepository
import javax.inject.Inject

// This class is a use case class that checks if a product is a favorite product of a user
class CheckFavoriteProductUseCase @Inject constructor(
    private val favoriteProductsRepository: FavoriteProductsRepository
){
    suspend operator fun invoke(userId: Long, productId: Int) : Boolean{
        return favoriteProductsRepository.isFavorite(userId, productId)
    }
}