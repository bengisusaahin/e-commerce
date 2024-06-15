package com.bengisusahin.e_commerce.di.usecase

import com.bengisusahin.e_commerce.repository.ProductRepository
import javax.inject.Inject

class GetSingleProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke(productId: Int) = productRepository.getSingleProduct(productId)
}