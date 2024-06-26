package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataCart.Carts
import com.bengisusahin.e_commerce.di.usecase.cart.GetCartByUserIdUseCase
import com.bengisusahin.e_commerce.di.usecase.fav.FavoriteProductUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// This class is a ViewModel class for the orders fragment
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getCartByUserIdUseCase: GetCartByUserIdUseCase,
    private val favoriteProductUseCase: FavoriteProductUseCase
) : ViewModel() {
    // LiveData to hold the cart data
    private val _cart = MutableLiveData<ScreenState<Carts>>()
    val cart : LiveData<ScreenState<Carts>> get() = _cart

    init {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            getCartByUserId(userId)
        }
    }

    // This function gets the cart data by the user id
    private fun getCartByUserId(userId: Long){
        viewModelScope.launch {
            getCartByUserIdUseCase.invoke(userId).collectLatest {
                when(it){
                    is ResourceResponseState.Error -> _cart.value = ScreenState.Error(it.message ?: "Unknown error")
                    is ResourceResponseState.Loading -> _cart.value = ScreenState.Loading
                    is ResourceResponseState.Success -> _cart.value = ScreenState.Success(it.data!!)
                }
            }
        }
    }

    // This function gets the current user id
    private suspend fun getCurrentUserId(): Long {
        return favoriteProductUseCase.getCurrentUserId()
    }
}