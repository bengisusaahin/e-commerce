package com.bengisusahin.e_commerce.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bengisusahin.e_commerce.MainActivity
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.data.dataCart.AddToCartProduct
import com.bengisusahin.e_commerce.databinding.FragmentHomeBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.view.adapter.ProductAdapter
import com.bengisusahin.e_commerce.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

// Fragment for displaying products in the home screen
@AndroidEntryPoint
class HomeFragment : Fragment(), ProductAdapter.Listener {
    private  var _binding : FragmentHomeBinding ? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Show the action bar when the fragment is visible
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set the toolbar title
        val toolbarTitle = (activity as? MainActivity)?.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle?.text = getString(R.string.title_home)
        binding.recyclerviewHome.layoutManager = GridLayoutManager(requireContext(),2)

        // Observe the products LiveData and update the UI accordingly when the data changes
        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is ScreenState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ScreenState.Success -> {
                    // Update RecyclerView with product list
                    binding.progressBar.visibility = View.GONE
                    resource.uiData?.let { products ->
                        binding.recyclerviewHome.adapter = ProductAdapter(products, this, viewModel)
                    }
                }

                is ScreenState.Error -> {
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        // Get the category name from the arguments
        val categoryName = arguments?.getString("categoryName")
        if (categoryName != null) {
            viewModel.getProductsByCategory(categoryName)
        } else {
            viewModel.getAllProducts()
        }

        viewModel.addToCartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Loading -> {
                    Log.d("HomeFragment", "Adding product to cart...")
                }
                is ScreenState.Error -> {
                    Log.d("HomeFragment", "Error adding product to cart: ${state.message}")
                    Toast.makeText(context, "Product could not be added to cart", Toast.LENGTH_SHORT).show()
                }
                is ScreenState.Success -> {
                    Log.d("HomeFragment", "Product added to cart successfully: ${state.uiData}")
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemClick(product: Product) {
        navigateToProductDetail(product.id)
    }

    private fun navigateToProductDetail(productId: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(productId)
        findNavController().navigate(action)
    }

    // Add the product to the cart when the user clicks the add to cart button
    override fun addToCart(product: Product) {
        Log.d("HomeFragment", "Adding product to cart: ${product.id}, ${product.title}")
        viewModel.addToCart(listOf(AddToCartProduct(product.id, 1))) // assuming quantity is 1
    }

    // Clean up the binding object when the fragment is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
