package com.bengisusahin.e_commerce.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.data.dataProfile.Profile
import com.bengisusahin.e_commerce.di.usecase.fav.FavoriteProductUseCase
import com.bengisusahin.e_commerce.di.usecase.profile.GetProfileUseCase
import com.bengisusahin.e_commerce.di.usecase.profile.UpdateProfileUseCase
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// This class is a ViewModel class for the profile screen
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val favoriteProductUseCase: FavoriteProductUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel(){

    // MutableLiveData object for the profile screen state
    private val _profile = MutableLiveData<ScreenState<Profile>>()
    val profile : LiveData<ScreenState<Profile>> get() = _profile

    // MutableLiveData object for the update profile screen state
    private val _updateProfileState = MutableLiveData<ScreenState<Profile>>()
    val updateProfileState : LiveData<ScreenState<Profile>> get() = _updateProfileState
    init {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            getProfile(userId)
        }
    }

    // Function to fetch the profile of the current user
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
    // Function to update the profile of the current user
    fun updateProfile(profile: Profile){
        viewModelScope.launch {
            val userId = getCurrentUserId()
            updateProfileUseCase.invoke(userId, profile).collectLatest { result ->
                when(result){
                    is ResourceResponseState.Error -> {
                        Log.e("ProfileViewModel", "Error updating profile: ${result.message ?: "Unknown error"}")
                        _updateProfileState.value = ScreenState.Error(result.message ?: "Unknown error")
                    }
                    is ResourceResponseState.Loading -> {
                        Log.d("ProfileViewModel", "Updating profile...")
                        _updateProfileState.value = ScreenState.Loading
                    }
                    is ResourceResponseState.Success -> {
                        Log.d("ProfileViewModel", "Profile updated successfully")
                        _updateProfileState.value = ScreenState.Success(result.data!!)
                        Log.d("ProfileViewModel", "Updated profile: $profile")
                    }
                }
            }
        }
    }
    // Function to get the current user id
    private suspend fun getCurrentUserId(): Long {
        return favoriteProductUseCase.getCurrentUserId()
    }

}