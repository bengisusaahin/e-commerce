package com.bengisusahin.e_commerce.repository

import com.bengisusahin.e_commerce.data.dataCart.AddToCartProduct
import com.bengisusahin.e_commerce.data.dataCart.AddToCartRequest
import com.bengisusahin.e_commerce.data.dataCart.Cart
import com.bengisusahin.e_commerce.data.dataCart.Carts
import com.bengisusahin.e_commerce.service.CartService
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// This class is responsible for handling the data operations related to the cart
class CartRepository @Inject constructor(
    private val cartService: CartService
){
    fun addToCart(userId: Long, products: List<AddToCartProduct>): Flow<ResourceResponseState<Cart>> = flow {
        emit(ResourceResponseState.Loading())
        val addToCartRequest = AddToCartRequest(userId, products)
        val carts = cartService.addToCart(addToCartRequest)
        emit(ResourceResponseState.Success(carts))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }
    fun getCartByUserId(userId: Long): Flow<ResourceResponseState<Carts>> = flow {
        emit(ResourceResponseState.Loading())
        val carts = cartService.getCartByUserId(userId)
        emit(ResourceResponseState.Success(carts))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }
}