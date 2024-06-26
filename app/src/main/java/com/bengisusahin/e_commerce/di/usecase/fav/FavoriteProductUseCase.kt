package com.bengisusahin.e_commerce.di.usecase.fav

import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.repository.AuthRepository
import com.bengisusahin.e_commerce.repository.FavoriteProductsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

// This class is used to add a product to the favorite list
class FavoriteProductUseCase@Inject constructor(
    private val authRepository: AuthRepository,
    private val favoriteProductsRepository: FavoriteProductsRepository
){
    suspend operator fun invoke(product: Product): FavoriteProducts {
        val user = authRepository.getCurrentUser().first()
        return FavoriteProducts(0, user.id, product.id, product.title, product.price, product.images[0],product.description)
    }
    // TODO move another use case class
    // This function is used to get the current user id so it is not operator fun
    suspend fun getCurrentUserId(): Long {
        return authRepository.getCurrentUser().first().id
    }

    suspend fun getFid(userId: Long, productId: Int): Int? {
        return favoriteProductsRepository.getFid(userId, productId)
    }
}