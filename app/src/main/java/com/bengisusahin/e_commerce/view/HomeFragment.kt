package com.bengisusahin.e_commerce.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.databinding.FragmentHomeBinding
import com.bengisusahin.e_commerce.util.Resource
import com.bengisusahin.e_commerce.view.adapter.ProductAdapter
import com.bengisusahin.e_commerce.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), ProductAdapter.Listener {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel: ProductViewModel by viewModels()
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
                is Resource.Loading -> {
                    // Show loading indicator
                }

                is Resource.Success -> {
                    // Update RecyclerView with product list
                    resource.data?.let { products ->
                        binding.recyclerviewHome.adapter = ProductAdapter(products.products, this)
                    }
                }

                is Resource.Error -> {
                    // Show error message
                }
            }
        }
    }

    override fun onItemClick(product: Product) {
        //viewModel.addToCart(product)
    }

}