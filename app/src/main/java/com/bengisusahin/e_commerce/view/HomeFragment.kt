package com.bengisusahin.e_commerce.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.databinding.FragmentHomeBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.ProductAdapter
import com.bengisusahin.e_commerce.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment(), ProductAdapter.Listener {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var productAdapter : ProductAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewHome.layoutManager = GridLayoutManager(requireContext(),2)

        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is ScreenState.Loading -> {
                    // Show loading indicator
                }

                is ScreenState.Success -> {
                    // Update RecyclerView with product list
                    resource.uiData?.let { products ->
                        binding.recyclerviewHome.adapter = ProductAdapter(products, this, viewModel)
                    }
                }

                is ScreenState.Error -> {
                    // Show error message
                }
            }
        }
    }

    override fun onItemClick(product: Product) {
        // add product to cart
        //viewModel.addToCart(product)
        navigateToProductDetail(product.id)
    }

    private fun navigateToProductDetail(productId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(productId)
        findNavController().navigate(action)
    }

}
