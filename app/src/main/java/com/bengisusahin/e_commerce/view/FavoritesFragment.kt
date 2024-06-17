package com.bengisusahin.e_commerce.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bengisusahin.e_commerce.databinding.FragmentFavoritesBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.FavoriteProductsAdapter
import com.bengisusahin.e_commerce.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(){

    private val viewModel: FavoriteViewModel by viewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoriteProductsAdapter = FavoriteProductsAdapter(listOf()) { favoriteProduct ->
        // handle click event
        viewModel.deleteFavoriteProduct(favoriteProduct)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rwFavorite.layoutManager = LinearLayoutManager(context)
        binding.rwFavorite.adapter = favoriteProductsAdapter

        // Get userId from SharedPreferences
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref?.getLong("userId", 0L) ?: 0L

        // Call getAllFavoriteProducts with userId
        viewModel.getAllFavoriteProducts(userId)

        viewModel.favoriteProducts.observe(viewLifecycleOwner) { state ->
            // update the UI
            when (state) {
                is ScreenState.Loading -> {
                }

                is ScreenState.Success -> {
                    state.uiData?.let { products ->
                        Log.d("FavoritesFragment", "onViewCreated: $products")
                        favoriteProductsAdapter.updateFavoriteProducts(products)
                    }
                }

                is ScreenState.Error -> {
                    // show an error message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}