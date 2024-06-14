package com.bengisusahin.e_commerce.repository

import com.bengisusahin.e_commerce.data.Products
import com.bengisusahin.e_commerce.service.ProductService
import com.bengisusahin.e_commerce.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productService: ProductService
){
    fun getProducts(): Flow<Resource<Products>> = flow {
        emit(Resource.Loading())

        val products = productService.getProductsListFromApi()
        emit(Resource.Success(products))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(Resource.Error(it.message.toString()))
        }
}