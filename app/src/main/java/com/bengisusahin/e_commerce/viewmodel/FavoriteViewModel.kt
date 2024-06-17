package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.di.usecase.DeleteFavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.GetAllFavoriteProductsUseCase
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoriteProductsUseCase: GetAllFavoriteProductsUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteProductUseCase,
) : ViewModel() {
    private val _favoriteProducts = MutableLiveData<ScreenState<List<FavoriteProducts>>>()
    val favoriteProducts: LiveData<ScreenState<List<FavoriteProducts>>> get() = _favoriteProducts

    init {
        getAllFavoriteProducts()
        deleteFavoriteUseCase
    }

    private fun getAllFavoriteProducts() {
        viewModelScope.launch {
            try {
                _favoriteProducts.value = ScreenState.Loading
                getAllFavoriteProductsUseCase().collect { result ->
                    _favoriteProducts.value = ScreenState.Success(result)
                }
            } catch (e: Exception) {
                _favoriteProducts.value = ScreenState.Error(e.toString())
            }
        }
    }

    fun deleteFavoriteProduct(favoriteProducts: FavoriteProducts) {
        viewModelScope.launch {
            deleteFavoriteUseCase(favoriteProducts)
        }
    }
}
