package com.bengisusahin.e_commerce.service
import com.bengisusahin.e_commerce.data.dataCart.AddToCartRequest
import com.bengisusahin.e_commerce.data.dataCart.Cart
import com.bengisusahin.e_commerce.data.dataCart.Product
import com.bengisusahin.e_commerce.data.dataCart.Carts
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// CartService interface defines the methods for adding a product to the cart and getting the cart by user ID
interface CartService {
    // Service returns a Cart object when adding a product to the cart
    @POST("carts/add")
    suspend fun addToCart(@Body addToCartRequest: AddToCartRequest): Cart
    @GET("carts/user/{userId}")
    suspend fun getCartByUserId(@Path("userId") userId: Long): Carts
}