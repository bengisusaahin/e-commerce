package com.bengisusahin.e_commerce.di.usecase.category

import com.bengisusahin.e_commerce.repository.ProductRepository
import javax.inject.Inject

// This use case is used to get products by category
class GetProductsByCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke(category: String) = productRepository.getProductsByCategory(category)

}