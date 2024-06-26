package com.bengisusahin.e_commerce.di.usecase.cart

import com.bengisusahin.e_commerce.repository.CartRepository
import javax.inject.Inject

// This use case is used to get the cart of a user by their ID
class GetCartByUserIdUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    operator fun invoke(userId: Long) = cartRepository.getCartByUserId(userId)
}