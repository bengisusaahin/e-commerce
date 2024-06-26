package com.bengisusahin.e_commerce.di.usecase.category

import com.bengisusahin.e_commerce.repository.ProductRepository
import javax.inject.Inject

// This class is a use case class that is responsible for getting all categories
class GetAllCategoriesUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke() = productRepository.getAllCategories()
}
