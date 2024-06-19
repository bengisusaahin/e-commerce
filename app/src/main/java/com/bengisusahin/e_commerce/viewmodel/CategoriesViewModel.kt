package com.bengisusahin.e_commerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataCategories.Categories
import com.bengisusahin.e_commerce.di.usecase.GetAllCategoriesUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {
    private val _categories = MutableLiveData<ScreenState<Categories>>()
    val categories: LiveData<ScreenState<Categories>> get() = _categories

    init {
        getAllCategories()
    }

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
}
