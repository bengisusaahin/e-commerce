package com.bengisusahin.e_commerce.di.usecase

import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FavoriteProductUseCase@Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(product: Product): FavoriteProducts {
        val user = authRepository.getCurrentUser().first()
        return FavoriteProducts(0, user.id, product.id, product.title, product.price, product.images[0],product.description)
    }
}