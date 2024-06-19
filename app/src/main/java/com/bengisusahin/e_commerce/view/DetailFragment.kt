package com.bengisusahin.e_commerce.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.databinding.FragmentDetailBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.DetailImageViewPagerAdapter
import com.bengisusahin.e_commerce.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding ? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the product ID from the arguments
        val args = DetailFragmentArgs.fromBundle(requireArguments())
        val productId = args.productId

        // Fetch the product details
        viewModel.getProduct(productId)

        // Observe the product LiveData
        viewModel.product.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is ScreenState.Loading -> {
                    // Show loading indicator
                    binding.detailProgressBar.visibility = View.VISIBLE
                }
                is ScreenState.Success -> {
                    // Update UI with product details
                    val product = screenState.uiData
                    binding.detailProductTitle.text = product.title
                    binding.detailProductDescription.text = product.description
                    binding.detailProductPrice.text = product.price.toString()
                    binding.detailProductRating.rating = product.rating.toFloat()
                    binding.detailProgressBar.visibility = View.GONE
                    // TODO: Update your ViewPager2 with product images
                    viewPagerSetup(product)
                }
                is ScreenState.Error -> {
                    // Show error message
                    Toast.makeText(context, screenState.message, Toast.LENGTH_SHORT).show()
                    binding.detailProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun viewPagerSetup(product: Product) {
        binding.viewPagerProductImages.adapter = DetailImageViewPagerAdapter(product.images)
        TabLayoutMediator(binding.tabLayoutIndicator, binding.viewPagerProductImages) { _, _ -> }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}