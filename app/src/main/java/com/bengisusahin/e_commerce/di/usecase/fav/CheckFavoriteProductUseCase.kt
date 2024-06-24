package com.bengisusahin.e_commerce.di.usecase.fav

import com.bengisusahin.e_commerce.repository.FavoriteProductsRepository
import javax.inject.Inject

class CheckFavoriteProductUseCase @Inject constructor(
    private val favoriteProductsRepository: FavoriteProductsRepository
){
    suspend operator fun invoke(userId: Long, productId: Int) : Boolean{
        return favoriteProductsRepository.isFavorite(userId, productId)
    }
}