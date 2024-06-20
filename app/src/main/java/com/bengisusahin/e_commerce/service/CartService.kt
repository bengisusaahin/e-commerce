package com.bengisusahin.e_commerce.service
import com.bengisusahin.e_commerce.data.dataCart.Carts
import retrofit2.http.GET
import retrofit2.http.Path

interface CartService {
    @GET("carts/user/{userId}")
    suspend fun getCartByUserId(@Path("userId") userId: Long): Carts
}