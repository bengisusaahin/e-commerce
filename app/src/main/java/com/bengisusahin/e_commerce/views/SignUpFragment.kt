package com.bengisusahin.e_commerce.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

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

        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmailSignUp.text.toString()
            val password = binding.etPasswordSignUp.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(context,"Please fill in all fields",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signup", "createUserWithEmail:success")
                            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)

                            // navigate to home fragment
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signUpFail", "signInWithEmail:failure", task.exception)
                            Toast.makeText(context,"Authentication failed.",Toast.LENGTH_SHORT).show()
                            // navigate to sign up fragment
                        }
                    }
            }
            binding.textviewLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }
}