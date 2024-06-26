package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.di.usecase.fav.DeleteFavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.fav.FavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.fav.GetAllFavoriteProductsUseCase
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// This class is a ViewModel class for the favorite products. It is responsible for managing the favorite products
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteProductUseCase: FavoriteProductUseCase,
    private val getAllFavoriteProductsUseCase: GetAllFavoriteProductsUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteProductUseCase
) : ViewModel() {
    // MutableLiveData to hold the favorite products
    private val _favoriteProducts = MutableLiveData<ScreenState<List<FavoriteProducts>>>()
    val favoriteProducts: LiveData<ScreenState<List<FavoriteProducts>>> get() = _favoriteProducts

    init {
        getAllFavoriteProducts()
    }
    // Function to get all favorite products from the database
    private fun getAllFavoriteProducts() {
        viewModelScope.launch {
            try {
                val userId = getCurrentUserId()
                _favoriteProducts.value = ScreenState.Loading
                getAllFavoriteProductsUseCase(userId).collect { result ->
                    _favoriteProducts.value = ScreenState.Success(result)
                }
            } catch (e: Exception) {
                _favoriteProducts.value = ScreenState.Error(e.toString())
            }
        }
    }

    // Function to get the current user ID
    private suspend fun getCurrentUserId(): Long {
        return favoriteProductUseCase.getCurrentUserId()
    }

    fun deleteFavoriteProduct(favoriteProducts: FavoriteProducts) {
        viewModelScope.launch {
            deleteFavoriteUseCase(favoriteProducts)
        }
    }
}
