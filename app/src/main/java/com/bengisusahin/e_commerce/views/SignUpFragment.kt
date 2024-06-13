package com.bengisusahin.e_commerce.views

import android.os.Bundle
import android.util.Log
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
import com.bengisusahin.e_commerce.data.User
import com.bengisusahin.e_commerce.databinding.FragmentSignUpBinding
import com.bengisusahin.e_commerce.util.Resource
import com.bengisusahin.e_commerce.util.FieldValidation
import com.bengisusahin.e_commerce.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// AndroidEntryPoint annotation is used for injecting dependencies to the fragment
@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignUp.setOnClickListener {
                val user = User(
                    etName.text.toString(),
                    etEmailSignUp.text.toString()
                )
                val password = etPasswordSignUp.text.toString()
                viewModel.createUserWithEmailAndPassword(user, password)
            }

            textviewLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }

        lifecycleScope.launch {
            viewModel.signUpState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {
                        Log.d("SignUpFragment", "User signed up successfully ${state.data.toString()}")
                        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                    }

                    is Resource.Error -> {
                        Log.d("SignUpFragment", "Error: ${state.message}")
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.signUpFormState.collect { state ->
                if (state.emailError is FieldValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.etEmailSignUp.apply {
                            error = state.emailError.message
                            requestFocus()
                        }
                    }
                }
                if (state.passwordError is FieldValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.etPasswordSignUp.apply {
                            error = state.passwordError.message
                            requestFocus()
                        }
                    }
                }
            }
        }
    }
}