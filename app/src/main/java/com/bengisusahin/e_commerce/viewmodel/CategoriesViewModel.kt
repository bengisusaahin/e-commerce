package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataCategories.Categories
import com.bengisusahin.e_commerce.data.dataCategories.CategoriesItem
import com.bengisusahin.e_commerce.di.usecase.category.GetAllCategoriesUseCase
import com.bengisusahin.e_commerce.di.usecase.category.SearchCategoryUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// This class is a ViewModel class that is used to manage the data in the CategoriesFragment
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAllCategoriesUseCase,
    private val searchCategoryUseCase: SearchCategoryUseCase
) : ViewModel() {
    // This LiveData is used to observe the categories from the API
    private val _categories = MutableLiveData<ScreenState<Categories>>()
    val categories: LiveData<ScreenState<Categories>> get() = _categories

    // This LiveData is used to observe the search result from the API
    private val _searchState = MutableLiveData<ScreenState<List<CategoriesItem>>>()
    val searchState: LiveData<ScreenState<List<CategoriesItem>>> get() = _searchState

    // This method is used to get all categories from the API
    init {
        getAllCategories()
    }

    // This method is used to get all categories from the API
    private fun getAllCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collectLatest {
                when (it) {
                    is ResourceResponseState.Loading -> _categories.postValue(ScreenState.Loading)
                    is ResourceResponseState.Error -> _categories.postValue(ScreenState.Error(it.message ?: "Unknown error"))
                    is ResourceResponseState.Success -> _categories.postValue(ScreenState.Success(it.data ?: Categories()))
                }
            }
        }
    }

    // This method is used to search for a category in the API
    fun searchCategory(query: String) {
        viewModelScope.launch {
            searchCategoryUseCase(query).collectLatest {
                when (it) {
                    is ResourceResponseState.Loading -> _searchState.postValue(ScreenState.Loading)
                    is ResourceResponseState.Error -> _searchState.postValue(ScreenState.Error(it.message ?: "Unknown error"))
                    is ResourceResponseState.Success -> _searchState.postValue(ScreenState.Success(it.data ?: listOf()))
                }
            }
        }
    }
}
