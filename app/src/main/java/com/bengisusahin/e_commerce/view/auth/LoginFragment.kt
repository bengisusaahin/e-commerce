package com.bengisusahin.e_commerce.view.auth

import android.os.Bundle
import android.util.Log
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

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var composeView: ComposeView
    private val viewModel by viewModels<AuthViewModel>()
    @Inject lateinit var sharedPrefManager: SharedPrefManager

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
            val loginState by viewModel.loginState.collectAsState(initial = ResourceResponseState.Loading())
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
                    state = loginState,
                    formState = formState,
                    username = username ?: "",
                    password = password ?: "",
                    rememberMe = rememberMe,
                    onLoginClick = { username, password, rememberMe ->
                        viewModel.login(username, password, rememberMe)
                    }
                )
        }
        lifecycleScope.launch {
            viewModel.loginState.collect {
                when (it) {
                    is ResourceResponseState.Loading -> {
                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is ResourceResponseState.Success -> {
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