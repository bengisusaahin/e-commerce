package com.bengisusahin.e_commerce.repository

import android.util.Log
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.data.Products
import com.bengisusahin.e_commerce.service.ProductService
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productService: ProductService
){
    fun getAllProducts(): Flow<ResourceResponseState<Products>> = flow {
        emit(ResourceResponseState.Loading())

        val products = productService.getProductsListFromApi()
        emit(ResourceResponseState.Success(products))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }

    fun getSingleProduct(productId: Int): Flow<ResourceResponseState<Product>> = flow {
        emit(ResourceResponseState.Loading())

        val product = productService.getSingleProductFromApi(productId)
        emit(ResourceResponseState.Success(product))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
    }

    fun searchProducts(query: String): Flow<ResourceResponseState<List<Product>>> = flow {
        emit(ResourceResponseState.Loading())
        try {
            Log.d("search", "Searching for products with query: $query")
            val response = productService.searchProducts(query)
            emit(ResourceResponseState.Success(response.products))
        } catch (e: Exception) {
            Log.e("search", "Error searching for products: ${e.message}", e)
            emit(ResourceResponseState.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }
}