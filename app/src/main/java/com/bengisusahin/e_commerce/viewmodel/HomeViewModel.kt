package com.bengisusahin.e_commerce.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.data.dataCart.AddToCartProduct
import com.bengisusahin.e_commerce.data.dataCart.Cart
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.di.usecase.fav.CheckFavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.fav.DeleteFavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.fav.FavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.GetAllProductsUseCase
import com.bengisusahin.e_commerce.di.usecase.cart.AddToCartUseCase
import com.bengisusahin.e_commerce.di.usecase.category.GetProductsByCategoryUseCase
import com.bengisusahin.e_commerce.di.usecase.fav.InsertFavoriteProductUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val favoriteProductUseCase: FavoriteProductUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val insertFavoriteProductUseCase: InsertFavoriteProductUseCase,
    private val deleteFavoriteProductUseCase: DeleteFavoriteProductUseCase,
    private val checkFavoriteProductUseCase: CheckFavoriteProductUseCase,
    private val addToCartUseCase: AddToCartUseCase
): ViewModel(){

    private val _products = MutableLiveData<ScreenState<List<Product>>>()
    val products : LiveData<ScreenState<List<Product>>> get() = _products

    private val _addToCartState = MutableLiveData<ScreenState<Cart>>()
    val addToCartState : LiveData<ScreenState<Cart>> get() = _addToCartState

    fun getAllProducts(){
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

    fun insertFavoriteProduct(product: Product) {
        viewModelScope.launch {
            val favoriteProduct = favoriteProductUseCase(product)
            insertFavoriteProductUseCase(favoriteProduct)
        }
    }

    suspend fun deleteFavoriteProduct(product: Product): Int {
        val userId = favoriteProductUseCase.getCurrentUserId()
        val fid = favoriteProductUseCase.getFid(userId, product.id)
        if (fid != null) {
            val favoriteProduct = FavoriteProducts(fid, userId, product.id, product.title, product.price, product.images[0],product.description)
            Log.d("deleteFavoriteProduct", "FavoriteProduct: $favoriteProduct")
            val deleteResult = deleteFavoriteProductUseCase(favoriteProduct)
            Log.d("deleteFavoriteProduct", "Delete result: $deleteResult")
            return deleteResult
        } else {
            Log.d("deleteFavoriteProduct", "Fid is null")
            return -1
        }
    }

    suspend fun isFavorite(productId: Int): Boolean {
        val userId = favoriteProductUseCase.getCurrentUserId()
        return checkFavoriteProductUseCase(userId, productId)
    }

    fun getProductsByCategory(category: String){
        viewModelScope.launch {
            getProductsByCategoryUseCase(category).collectLatest { resource ->
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
    fun addToCart(products: List<AddToCartProduct>) {
        viewModelScope.launch {
            val userId = favoriteProductUseCase.getCurrentUserId()
            addToCartUseCase(userId, products).collectLatest { resource ->
                _addToCartState.value = when (resource) {
                    is ResourceResponseState.Loading -> ScreenState.Loading
                    is ResourceResponseState.Error -> ScreenState.Error(resource.message ?: "Unknown error")
                    is ResourceResponseState.Success -> {
                        if (resource.data != null) {
                            ScreenState.Success(resource.data)
                        } else {
                            ScreenState.Error("Data is null")
                        }
                    }
                }
            }
        }
    }
}