package com.bengisusahin.e_commerce.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataProfile.Profile
import com.bengisusahin.e_commerce.di.usecase.fav.FavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.profile.GetProfileUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val favoriteProductUseCase: FavoriteProductUseCase
) : ViewModel(){

    private val _profile = MutableLiveData<ScreenState<Profile>>()
    val profile : LiveData<ScreenState<Profile>> get() = _profile
    init {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            getProfile(userId)
        }
    }

    private fun getProfile(userId: Long){
        viewModelScope.launch {
            Log.d("ProfileViewModel", "Fetching profile for user id: $userId")
            getProfileUseCase.invoke(userId).collectLatest { result ->
                when(result){
                    is ResourceResponseState.Error -> {
                        Log.e("ProfileViewModel", "Error fetching profile: ${result.message ?: "Unknown error"}")
                        _profile.value = ScreenState.Error(result.message ?: "Unknown error")
                    }
                    is ResourceResponseState.Loading -> {
                        Log.d("ProfileViewModel", "Loading profile...")
                        _profile.value = ScreenState.Loading
                    }
                    is ResourceResponseState.Success -> {
                        Log.d("ProfileViewModel", "Profile fetched successfully")
                        _profile.value = ScreenState.Success(result.data!!)
                    }
                }
            }
        }
    }


    private suspend fun getCurrentUserId(): Long {
        return favoriteProductUseCase.getCurrentUserId()
    }

}