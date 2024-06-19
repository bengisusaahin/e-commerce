package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bengisusahin.e_commerce.databinding.FragmentCategoriesBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.viewmodel.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
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

        categoriesAdapter = CategoriesAdapter(listOf())

        val categoriesRecyclerView = binding.rwCategories
        categoriesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        categoriesRecyclerView.adapter = categoriesAdapter

        viewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ScreenState.Loading -> {
                    // Show loading indicator
                }
                is ScreenState.Success -> {
                    response.uiData?.let { newCategories ->
                        categoriesAdapter.updateCategories(newCategories)
                    }
                }
                is ScreenState.Error -> {
                    // Show error message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
