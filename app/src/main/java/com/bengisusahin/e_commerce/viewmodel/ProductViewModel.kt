package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel(){

    val products = productRepository.getProducts()
        .asLiveData(viewModelScope.coroutineContext)
}