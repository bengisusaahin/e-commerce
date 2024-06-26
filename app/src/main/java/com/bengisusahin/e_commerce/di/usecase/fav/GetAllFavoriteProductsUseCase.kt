package com.bengisusahin.e_commerce.di.usecase.fav

import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.repository.FavoriteProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// This use case is used to get all favorite products of a user
class GetAllFavoriteProductsUseCase @Inject constructor(
    private val favoriteProductsRepository: FavoriteProductsRepository
){
    operator fun invoke(userId: Long): Flow<List<FavoriteProducts>>{
        return favoriteProductsRepository.getAllFavoriteProducts(userId)
    }
}