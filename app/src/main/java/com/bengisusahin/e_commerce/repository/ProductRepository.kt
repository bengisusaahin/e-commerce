package com.bengisusahin.e_commerce.repository

import android.util.Log
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.data.dataProduct.Products
import com.bengisusahin.e_commerce.data.dataCategories.Categories
import com.bengisusahin.e_commerce.data.dataCategories.CategoriesItem
import com.bengisusahin.e_commerce.service.ProductService
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// Repository class for the products in the app that communicates with the ProductService
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

    fun getAllCategories(): Flow<ResourceResponseState<Categories>> = flow {
        emit(ResourceResponseState.Loading())
        val categories = productService.getAllCategoriesFromApi()
        emit(ResourceResponseState.Success(categories))
    }.flowOn(Dispatchers.IO)
        .catch { emit(ResourceResponseState.Error(it.message.toString())) }

    fun getProductsByCategory(category: String): Flow<ResourceResponseState<Products>> = flow {
        emit(ResourceResponseState.Loading())
        val modifiedCategoryName = category.replace(" ", "-")
        val products = productService.getProductsByCategoryFromApi(modifiedCategoryName)
        emit(ResourceResponseState.Success(products))
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

    fun searchCategory(query: String): Flow<ResourceResponseState<List<CategoriesItem>>> = flow {
        emit(ResourceResponseState.Loading())
        try {
            Log.d("search", "Searching for categories with query: $query")
            val categories = getAllCategories().firstOrNull(){ it is ResourceResponseState.Success }?.data?: emptyList()
            val filteredCategories = categories.filter { it.name.contains(query, ignoreCase = true) }
            emit(ResourceResponseState.Success(filteredCategories))
        } catch (e: Exception) {
            emit(ResourceResponseState.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(ResourceResponseState.Error(it.message.toString()))
        }
}