package com.bengisusahin.e_commerce.service

import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.data.Products
import com.bengisusahin.e_commerce.data.dataCategories.Categories
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProductsListFromApi(): Products
    @GET("products/{id}")
    suspend fun getSingleProductFromApi(@Path("id") id: Int): Product
    @GET("products/categories")
    suspend fun getAllCategoriesFromApi(): Categories
    @GET("products/category/{category}")
    suspend fun getProductsByCategoryFromApi(@Path("category") category: String): Products
    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): Products

}