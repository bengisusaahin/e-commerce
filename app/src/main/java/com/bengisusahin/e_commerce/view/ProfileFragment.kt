package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.dataProfile.Profile
import com.bengisusahin.e_commerce.databinding.FragmentProfileBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ProfileFragment", "onViewCreated is called")
        viewModel.profile.observe(viewLifecycleOwner) { state ->
            Log.d("ProfileFragment", "Observing profile LiveData: $state")
            when (state) {
                is ScreenState.Success -> {
                    Log.d("ProfileFragment", "Success state in profile LiveData")
                    displayProfile(state.uiData)
                }
                is ScreenState.Error -> {
                    Log.d("ProfileFragment", "Error state in profile LiveData: ${state.message}")
                    // handle error
                }
                is ScreenState.Loading -> {
                    Log.d("ProfileFragment", "Loading state in profile LiveData")
                    // show loading
                }
            }
        }
    }
    private fun displayProfile(profile: Profile) {
        binding?.apply {
            etFirstName?.text = Editable.Factory.getInstance().newEditable(profile.firstName)
            etLastName?.text = Editable.Factory.getInstance().newEditable(profile.lastName)
            etUserName?.text = Editable.Factory.getInstance().newEditable(profile.username)
            etGender?.text = Editable.Factory.getInstance().newEditable(profile.gender)
            etAge?.text = Editable.Factory.getInstance().newEditable(profile.age.toString())
            etPhone?.text = Editable.Factory.getInstance().newEditable(profile.phone)
            etBirthday?.text = Editable.Factory.getInstance().newEditable(profile.birthDate)
            etBloodGroup?.text = Editable.Factory.getInstance().newEditable(profile.bloodGroup)
            etAddress?.text = Editable.Factory.getInstance().newEditable(profile.address.address)
            etCity?.text = Editable.Factory.getInstance().newEditable(profile.address.city)
            etState?.text = Editable.Factory.getInstance().newEditable(profile.address.state)
            etCountry?.text = Editable.Factory.getInstance().newEditable(profile.address.country)
            Glide.with(requireContext())
                .load(profile.image)
                .placeholder(R.drawable.indicator)
                .into(binding.imageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}