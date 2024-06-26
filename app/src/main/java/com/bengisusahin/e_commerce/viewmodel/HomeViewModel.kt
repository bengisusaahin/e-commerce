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

// HilViewModel annotation is used to inject dependencies to the ViewModel class
// Inject annotation is used to inject dependencies to the ViewModel class
// ViewModel class is used to manage UI-related data in a lifecycle-conscious way
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

    // MutableLiveData is a data holder class that can be observed within a given lifecycle
    private val _products = MutableLiveData<ScreenState<List<Product>>>()
    val products : LiveData<ScreenState<List<Product>>> get() = _products

    // add to cart state to observe the state of the cart
    private val _addToCartState = MutableLiveData<ScreenState<Cart>>()
    val addToCartState : LiveData<ScreenState<Cart>> get() = _addToCartState

    // getAllProducts function is used to get all products from the server
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

    // insertFavoriteProduct function is used to insert a product to the favorite list
    fun insertFavoriteProduct(product: Product) {
        viewModelScope.launch {
            val favoriteProduct = favoriteProductUseCase(product)
            insertFavoriteProductUseCase(favoriteProduct)
        }
    }

    // deleteFavoriteProduct function is used to delete a product from the favorite list
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

    // isFavorite function is used to check if a product is in the favorite list
    suspend fun isFavorite(productId: Int): Boolean {
        val userId = favoriteProductUseCase.getCurrentUserId()
        return checkFavoriteProductUseCase(userId, productId)
    }

    // getProductsByCategory function is used to get products by category
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
    // addToCart function is used to add products to the cart
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