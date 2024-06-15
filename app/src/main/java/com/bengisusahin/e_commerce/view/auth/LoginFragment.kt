package com.bengisusahin.e_commerce.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.databinding.FragmentLoginBinding
import com.bengisusahin.e_commerce.dialog.setUpBottomSheetDialog
import com.bengisusahin.e_commerce.util.FieldValidation
import com.bengisusahin.e_commerce.util.Resource
import com.bengisusahin.e_commerce.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()
                viewModel.loginWithEmailAndPassword(email, password)
            }

            textviewSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

            tvForgotPassword.setOnClickListener {
                setUpBottomSheetDialog { email ->
                    viewModel.resetPassword(email)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.resetPasswordState.collect{
                when(it){
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        Snackbar.make(binding.root, "Reset password link sent to your email", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loginState.collect {
                when (it) {
                    is Resource.Loading -> {
                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loginFormState.collect { state ->
                if (state.emailError is FieldValidation.Error) {
                    binding.etEmail.apply {
                        error = state.emailError.message
                        requestFocus()
                    }
                }
                if (state.passwordError is FieldValidation.Error) {
                    binding.etPassword.apply {
                        error = state.passwordError.message
                        requestFocus()
                    }
                }
            }
        }
    }
}