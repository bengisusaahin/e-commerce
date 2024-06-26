package com.bengisusahin.e_commerce.di.usecase.cart

import com.bengisusahin.e_commerce.data.dataCart.AddToCartProduct
import com.bengisusahin.e_commerce.data.dataCart.Cart
import com.bengisusahin.e_commerce.repository.CartRepository
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// This class is a use case class for adding products to the cart
class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    operator fun invoke(userId: Long, products: List<AddToCartProduct>): Flow<ResourceResponseState<Cart>> {
        return cartRepository.addToCart(userId, products)
    }
}