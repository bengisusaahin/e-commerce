package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.di.usecase.GetAllProductsUseCase
import com.bengisusahin.e_commerce.di.usecase.SearchProductsUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// This class is a ViewModel class for the search feature of the application
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
): ViewModel(){

    // This variable is a MutableLiveData object which holds the state of the products
    private val _products = MutableLiveData<ScreenState<List<Product>>>()
    val products : LiveData<ScreenState<List<Product>>> get() = _products

    // This variable is a MutableStateFlow object which holds the state of the search
    private val _searchState = MutableStateFlow<ResourceResponseState<List<Product>>>(ResourceResponseState.Loading())
    val searchState: StateFlow<ResourceResponseState<List<Product>>> = _searchState

    init {
        getAllProducts()
    }

    // This function is used to get all products
    private fun getAllProducts(){
        viewModelScope.launch {
            getAllProductsUseCase().collectLatest { resource ->
                when (resource) {
                    is ResourceResponseState.Loading -> _products.postValue(ScreenState.Loading)
                    is ResourceResponseState.Error -> _products.postValue(ScreenState.Error(resource.message ?: "Unknown error"))
                    is ResourceResponseState.Success -> {
                        val productList = resource.data?.products
                        _products.postValue(ScreenState.Success(productList ?: listOf()))
                    }
                }
            }
        }
    }
    // This function is used to search products by query
    fun searchProducts(query: String) {
        viewModelScope.launch {
            searchProductsUseCase(query).collect { resource ->
                when (resource) {
                    is ResourceResponseState.Loading -> _searchState.value = ResourceResponseState.Loading()
                    is ResourceResponseState.Error -> _searchState.value = ResourceResponseState.Error(resource.message ?: "Unknown error")
                    is ResourceResponseState.Success -> {
                        val productList = resource.data
                        _searchState.value = ResourceResponseState.Success(productList ?: listOf())
                    }
                }
            }
        }
    }
}