package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bengisusahin.e_commerce.MainActivity
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.databinding.FragmentCategoriesBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.viewmodel.CategoriesViewModel
import com.bengisusahin.e_commerce.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarTitle = (activity as? MainActivity)?.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle?.text = getString(R.string.title_categories)

        categoriesAdapter = CategoriesAdapter(listOf()){ categoryName ->
            // Call getProductsByCategory when a category is clicked
            homeViewModel.getProductsByCategory(categoryName)

            // Navigate to the products fragment with the selected category
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToHomeFragment(categoryName)
            findNavController().navigate(action)
        }

        val categoriesRecyclerView = binding.rwCategories
        categoriesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        categoriesRecyclerView.adapter = categoriesAdapter

        observeCategoriesState()

        // Handle search
        binding.searchViewCategories.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchCategory(query)
                }else{
                    // If the search query is empty, reload the original categories
                    observeCategoriesState()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    viewModel.searchCategory(newText)
                } else {
                    // If the search query is empty, reload the original categories
                    observeCategoriesState()
                }
                return true
            }
        })
        binding.searchViewCategories.isIconifiedByDefault = false

        observeSearchState()
    }

    private fun observeCategoriesState() {
        viewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.uiData?.let { newCategories ->
                        categoriesAdapter.updateCategories(newCategories)
                    }
                }
                is ScreenState.Error -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun observeSearchState() {
        viewModel.searchState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.uiData?.let { searchResults ->
                        categoriesAdapter.updateCategories(searchResults)
                    }
                }
                is ScreenState.Error -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
