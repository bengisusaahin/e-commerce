package com.bengisusahin.e_commerce.view

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.MainActivity
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.databinding.FragmentFavoritesBinding
import com.bengisusahin.e_commerce.util.RecyclerViewSwipe
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.FavoriteProductsAdapter
import com.bengisusahin.e_commerce.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

// FavoritesFragment class is created to display the favorite products of the user.
@AndroidEntryPoint
class FavoritesFragment : Fragment(){

    private val viewModel: FavoriteViewModel by viewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoriteProductsAdapter = FavoriteProductsAdapter(listOf()) { favoriteProduct ->
        // handle click event
        navigateToProductDetail(favoriteProduct)
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
        val toolbarTitle = (activity as? MainActivity)?.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle?.text = getString(R.string.title_favorites)

        binding.rwFavorite.layoutManager = LinearLayoutManager(context)
        binding.rwFavorite.adapter = favoriteProductsAdapter

        // Swipe to delete functionality for favorite products
        val swipeHandler = object :  RecyclerViewSwipe() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favoriteProduct = favoriteProductsAdapter.getFavoriteProductAt(position)
                viewModel.deleteFavoriteProduct(favoriteProduct)
            }

            // Draw the delete icon and background color for the swipe action
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rwFavorite)

        // Observe the favorite products and update the UI accordingly
        viewModel.favoriteProducts.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Success -> {
                    state.uiData?.let { products ->
                        binding.progressBar.visibility = View.GONE
                        if (products.isEmpty()) {
                            Toast.makeText( context,"No Favorites Yet",Toast.LENGTH_LONG).show()
                        } else {
                            Log.d("FavoritesFragment", "onViewCreated: $products")
                            favoriteProductsAdapter.updateFavoriteProducts(products)
                        }
                    }
                }
                is ScreenState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    // Navigate to the product detail fragment when a favorite product is clicked
    private fun navigateToProductDetail(favoriteProduct: FavoriteProducts) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(favoriteProduct.pid)
        findNavController().navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}