package com.bengisusahin.e_commerce.di.usecase

import android.util.Log
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.repository.ProductRepository
import com.bengisusahin.e_commerce.util.ResourceResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// This class is a use case class for searching products
class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(query: String): Flow<ResourceResponseState<List<Product>>> = flow {
        emit(ResourceResponseState.Loading())
        try {
            Log.d("search", "SearchProductsUseCase invoked with query: $query")
            repository.searchProducts(query).collect { resource ->
                emit(resource)
            }
        } catch (e: Exception) {
            emit(ResourceResponseState.Error(e.message ?: "Unknown error"))
        }
    }
}
