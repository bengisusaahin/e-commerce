package com.bengisusahin.e_commerce.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.util.FieldValidation
import com.bengisusahin.e_commerce.util.FormState
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.SharedPrefManager
import com.bengisusahin.e_commerce.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// This fragment is responsible for the login screen of the application
@AndroidEntryPoint
class LoginFragment : Fragment() {
    // ComposeView is used to display the login screen UI which is built with Jetpack Compose
    private lateinit var composeView: ComposeView
    private val viewModel by viewModels<AuthViewModel>()
    @Inject lateinit var sharedPrefManager: SharedPrefManager
    private var isLoadingToastShown = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        composeView.setContent {
            val formState by viewModel.loginFormState.collectAsState(
                initial = FormState(
                    usernameError = FieldValidation.Success,
                    passwordError = FieldValidation.Success
                )
            )
            val rememberMe = sharedPrefManager.fetchRememberMe()
            val username = if (rememberMe) sharedPrefManager.fetchUsername() else ""
            val password = if (rememberMe) sharedPrefManager.fetchPassword() else ""

                LoginScreen(
                    formState = formState,
                    username = username ?: "",
                    password = password ?: "",
                    rememberMe = rememberMe,
                    onLoginClick = { username, password, rememberMe ->
                        viewModel.login(username, password, rememberMe)
                    }
                )
        }
        // Observe the login state and navigate to the home screen if the login is successful
        lifecycleScope.launch {
            viewModel.loginState.collect {
                when (it) {
                    is ResourceResponseState.Loading -> {
                        if (!isLoadingToastShown) {
                            Toast.makeText(requireContext(), "You are logging in...", Toast.LENGTH_SHORT).show()
                            isLoadingToastShown = true
                        }
                    }
                    is ResourceResponseState.Success -> {
                        sharedPrefManager.saveFirstName(it.data!!.firstName)
                        sharedPrefManager.saveUserImage(it.data.image)
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    is ResourceResponseState.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}