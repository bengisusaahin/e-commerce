package com.bengisusahin.e_commerce.di.usecase.cart

import com.bengisusahin.e_commerce.repository.CartRepository
import javax.inject.Inject

class GetCartByUserIdUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(userId: Long) = cartRepository.getCartByUserId(userId)
}