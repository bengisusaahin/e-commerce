package com.bengisusahin.e_commerce.di.usecase.category

import com.bengisusahin.e_commerce.repository.ProductRepository
import javax.inject.Inject

// This class is a use case class that is used to search for a category
class SearchCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke(query: String) = productRepository.searchCategory(query)
}