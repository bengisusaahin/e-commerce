package com.bengisusahin.e_commerce.service

import com.bengisusahin.e_commerce.data.Products
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    suspend fun getProductsListFromApi(): Products
}