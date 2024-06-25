package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bengisusahin.e_commerce.data.dataCart.AddToCartProduct
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.databinding.FragmentSearchBinding
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.SearchAdapter
import com.bengisusahin.e_commerce.viewmodel.HomeViewModel
import com.bengisusahin.e_commerce.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.Listener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rwSearch.layoutManager = GridLayoutManager(requireContext(), 2)
        searchAdapter = SearchAdapter(listOf(), this, homeViewModel)
        binding.rwSearch.adapter = searchAdapter

        // Observe the products LiveData
        viewModel.products.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Error -> {
                    Toast.makeText(context, screenState.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                is ScreenState.Success -> {
                    // Update the adapter with the new product list
                    screenState.uiData?.let { products ->
                        searchAdapter.updateData(products)
                    }
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        // Observe the searchState StateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collect { resourceResponseState ->
                when (resourceResponseState) {
                    is ResourceResponseState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResourceResponseState.Error -> {
                        Toast.makeText(context, resourceResponseState.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    is ResourceResponseState.Success -> {
                        // Update the adapter with the new product list
                        Log.d("search", "onViewCreated: ${resourceResponseState.data}")
                        resourceResponseState.data?.let { searchAdapter.updateData(it) }
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        // Set up the SearchView to call viewModel.searchProducts when a query is submitted
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchProducts(it)
                    Log.d("search", "onQueryTextSubmit: $it")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchProducts(it)
                    Log.d("search", "onQueryTextChange: $it")
                }
                return true
            }
        })
        binding.searchView.isIconifiedByDefault = false

        homeViewModel.addToCartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Loading -> {
                    Log.d("SearchFragment", "Adding product to cart...")
                }
                is ScreenState.Error -> {
                    Log.d("SearchFragment", "Error adding product to cart: ${state.message}")
                    Toast.makeText(context, "Product could not be added to cart", Toast.LENGTH_SHORT).show()
                }
                is ScreenState.Success -> {
                    Log.d("SearchFragment", "Product added to cart successfully: ${state.uiData}")
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }    }

    override fun onItemClick(product: Product) {
        // Handle item click here
        navigateToProductDetail(product.id)
    }

    private fun navigateToProductDetail(productId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(productId)
        findNavController().navigate(action)
    }

    override fun addToCart(product: Product) {
        Log.d("HomeFragment", "Adding product to cart: ${product.id}, ${product.title}")
        homeViewModel.addToCart(listOf(AddToCartProduct(product.id, 1))) // assuming quantity is 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}