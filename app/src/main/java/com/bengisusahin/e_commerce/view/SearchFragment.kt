package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.databinding.FragmentSearchBinding
import com.bengisusahin.e_commerce.util.ResourceResponseState
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.SearchAdapter
import com.bengisusahin.e_commerce.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.Listener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()

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
        searchAdapter = SearchAdapter(listOf(), this)
        binding.rwSearch.adapter = searchAdapter

        // Observe the products LiveData
        viewModel.products.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is ScreenState.Loading -> {
                    // TODO: Show loading state
                }
                is ScreenState.Error -> {
                    // TODO: Show error state
                }
                is ScreenState.Success -> {
                    // Update the adapter with the new product list
                    screenState.uiData?.let { products ->
                        searchAdapter.updateData(products)
                    }
                }
            }
        }

        // Observe the searchState StateFlow
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collect { resourceResponseState ->
                when (resourceResponseState) {
                    is ResourceResponseState.Loading -> {
                        // TODO: Show loading state
                    }
                    is ResourceResponseState.Error -> {
                        // TODO: Show error state
                    }
                    is ResourceResponseState.Success -> {
                        // Update the adapter with the new product list
                        Log.d("search", "onViewCreated: ${resourceResponseState.data}")
                        resourceResponseState.data?.let { searchAdapter.updateData(it) }
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
    }

    override fun onItemClick(product: Product) {
        // Handle item click here
        navigateToProductDetail(product.id)
    }

    private fun navigateToProductDetail(productId: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(productId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}