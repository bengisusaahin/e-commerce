package com.bengisusahin.e_commerce.di.usecase.category

import com.bengisusahin.e_commerce.repository.ProductRepository
import javax.inject.Inject

class SearchCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke(query: String) = productRepository.searchCategory(query)
}