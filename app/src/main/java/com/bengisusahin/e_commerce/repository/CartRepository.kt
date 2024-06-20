package com.bengisusahin.e_commerce.repository

import com.bengisusahin.e_commerce.data.dataCart.Carts
import com.bengisusahin.e_commerce.service.CartService
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartService: CartService
){
    fun getCartByUserId(userId: Long): Flow<ResourceResponseState<Carts>> = flow {
        emit(ResourceResponseState.Loading())
        val carts = cartService.getCartByUserId(userId)
        emit(ResourceResponseState.Success(carts))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }
}