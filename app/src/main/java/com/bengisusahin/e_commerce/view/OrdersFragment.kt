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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bengisusahin.e_commerce.MainActivity
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.databinding.FragmentOrdersBinding
import com.bengisusahin.e_commerce.util.ScreenState
import com.bengisusahin.e_commerce.util.SharedPrefManager
import com.bengisusahin.e_commerce.view.adapter.OrderRecyclerViewAdapter
import com.bengisusahin.e_commerce.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var adapter: OrderRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("OrdersFragment", "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("OrdersFragment", "onCreateView called")
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarTitle = (activity as? MainActivity)?.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle?.text = getString(R.string.title_orders)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        Log.d("OrdersFragment", "setupRecyclerView called")
        adapter = OrderRecyclerViewAdapter(listOf(), this)
        binding.rvCartProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.cart.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Loading -> {
                    // Show loading indicator
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Success -> {
                    // Hide loading indicator and update the RecyclerView
                    binding.progressBar.visibility = View.GONE
                    state.uiData?.let { newCart ->
                        adapter.updateCart(newCart.carts)
                    }
                }
                is ScreenState.Error -> {
                    // Hide loading indicator and show error message
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
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