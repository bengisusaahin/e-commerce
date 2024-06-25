package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.dataProfile.Address
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
        setUpObservers()

        binding.ibSaveUpdate.setOnClickListener {
            updateProfile()
        }
    }

    private fun setUpObservers() {
        viewModel.profile.observe(viewLifecycleOwner) { state ->
            Log.d("ProfileFragment", "Observing profile LiveData: $state")
            when (state) {
                is ScreenState.Success -> {
                    Log.d("ProfileFragment", "Success state in profile LiveData")
                    displayProfile(state.uiData)
                    binding.progressBar.visibility = View.GONE
                }
                is ScreenState.Error -> {
                    Log.d("ProfileFragment", "Error state in profile LiveData: ${state.message}")
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                is ScreenState.Loading -> {
                    Log.d("ProfileFragment", "Loading state in profile LiveData")
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.updateProfileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Success -> {
                    // Handle success state after update
                    Log.d("ProfileFragment", "Profile updated successfully")
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    // Optionally, fetch profile again after update
                    displayProfile(state.uiData)
                    binding.progressBar.visibility = View.GONE
                }
                is ScreenState.Error -> {
                    // Handle error state after update
                    Log.e("ProfileFragment", "Error updating profile: ${state.message}")
                    Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
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

    private fun updateProfile() {
        val updatedProfile = Profile(
            firstName = binding.etFirstName.text.toString(),
            lastName = binding.etLastName.text.toString(),
            username = binding.etUserName.text.toString(),
            gender = binding.etGender.text.toString(),
            age = binding.etAge.text.toString().toIntOrNull() ?: 0,
            phone = binding.etPhone.text.toString(),
            birthDate = binding.etBirthday.text.toString(),
            bloodGroup = binding.etBloodGroup.text.toString(),
            address = Address(
                address = binding.etAddress.text.toString(),
                city = binding.etCity.text.toString(),
                state = binding.etState.text.toString(),
                country = binding.etCountry.text.toString()
            )
        )
        viewModel.updateProfile(updatedProfile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}