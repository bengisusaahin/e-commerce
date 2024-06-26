package com.bengisusahin.e_commerce.di.usecase

import com.bengisusahin.e_commerce.repository.ProductRepository
import javax.inject.Inject

// This use case is used to get all products from the repository
class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke() = productRepository.getAllProducts()
}