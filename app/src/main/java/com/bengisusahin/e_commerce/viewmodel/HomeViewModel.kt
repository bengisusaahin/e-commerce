package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.di.usecase.GetAllProductsUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase
): ViewModel(){

    private val _products = MutableLiveData<ScreenState<List<Product>>>()
    val products : LiveData<ScreenState<List<Product>>> get() = _products

    init {
        getAllProducts()
    }
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
}