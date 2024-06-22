package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.di.usecase.GetSingleProductUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getSingleProductUseCase: GetSingleProductUseCase
): ViewModel(){
    private val _product = MutableLiveData<ScreenState<Product>>()
    val product: LiveData<ScreenState<Product>> get() = _product
    fun getProduct(id: Int){
        viewModelScope.launch {
            getSingleProductUseCase.invoke(id).collectLatest {
                when(it){
                    is ResourceResponseState.Error -> _product.value = ScreenState.Error(it.message ?: "Unknown error")
                    is ResourceResponseState.Loading -> _product.value = ScreenState.Loading
                    is ResourceResponseState.Success -> _product.value = ScreenState.Success(it.data!!)
                }
            }
        }
    }
}