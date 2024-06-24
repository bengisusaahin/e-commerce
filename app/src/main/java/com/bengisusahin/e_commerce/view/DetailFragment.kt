package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.dataCart.AddToCartProduct
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.databinding.FragmentDetailBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.DetailImageViewPagerAdapter
import com.bengisusahin.e_commerce.viewmodel.DetailViewModel
import com.bengisusahin.e_commerce.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding ? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

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
                    updateProductDetails(product)

                    // Check if the product is in favorites
                    checkIfProductIsFavorite(product)
                    handleFavoriteCheckbox(product)

                    // Add product to cart when button is clicked
                    binding.btnAddToCart.setOnClickListener {
                        homeViewModel.addToCart(listOf(AddToCartProduct(product.id, 1))) // assuming quantity is 1
                    }
                }
                is ScreenState.Error -> {
                    // Show error message
                    Toast.makeText(context, screenState.message, Toast.LENGTH_SHORT).show()
                    binding.detailProgressBar.visibility = View.GONE
                }
            }
        }
        // Observe the addToCart LiveData
        observeAddToCart()
    }

    private fun updateProductDetails(product: Product) {
        binding.apply {
            detailProductTitle.text = product.title
            detailProductDescription.text = product.description
            detailProductPrice.text = product.price.toString()
            detailProductRating.rating = product.rating.toFloat()
            detailProgressBar.visibility = View.GONE
            viewPagerSetup(product)
        }
    }

    private fun checkIfProductIsFavorite(product: Product) {
        homeViewModel.viewModelScope.launch {
            val isFavorite = homeViewModel.isFavorite(product.id)
            Log.d("isFavorite", "Is product favorite: $isFavorite")
            binding.checkBoxFavorite.isChecked = isFavorite
            Log.d("fav", "bind: $isFavorite")
        }
    }

    private fun handleFavoriteCheckbox(product: Product) {
        binding.checkBoxFavorite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // If the checkbox is now checked, set the favorite icon and add the product to favorites
                binding.checkBoxFavorite.setButtonDrawable(R.drawable.ic_like_filled)
                homeViewModel.insertFavoriteProduct(product)
            } else {
                // If the checkbox is now unchecked, set the not favorite icon and remove the product from favorites
                binding.checkBoxFavorite.setButtonDrawable(R.drawable.ic_like)
                homeViewModel.viewModelScope.launch {
                    val deleteResult = homeViewModel.deleteFavoriteProduct(product)
                    if (deleteResult == 0) {
                        Log.d("deleteFavoriteProduct", "Failed to delete product from favorites")
                    } else {
                        Log.d("deleteFavoriteProduct", "Product successfully deleted from favorites")
                    }
                }
            }
        }
    }

    private fun observeAddToCart() {
        homeViewModel.addToCartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Loading -> {
                    // Show loading indicator
                    binding.detailProgressBar.visibility = View.VISIBLE
                }
                is ScreenState.Error -> {
                    // Show error message
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    binding.detailProgressBar.visibility = View.GONE
                }
                is ScreenState.Success -> {
                    // Show success message
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
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