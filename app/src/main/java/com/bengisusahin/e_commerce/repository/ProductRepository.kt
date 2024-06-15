package com.bengisusahin.e_commerce.repository

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
}