package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.dataCart.Cart
import com.bengisusahin.e_commerce.databinding.OrderRecyclerRowBinding
import com.bengisusahin.e_commerce.view.OrdersFragmentDirections

// Adapter for the recycler view in the OrdersFragment
class OrderRecyclerViewAdapter(private var cart : List<Cart>, private val fragment : Fragment)
    : RecyclerView.Adapter<OrderRecyclerViewAdapter.CartRowHolder>() {

        inner class CartRowHolder(val binding : OrderRecyclerRowBinding ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(cart: Cart){
                binding.tvCartId.text = "Cart ID: ${cart.id}"
                binding.tvCartTotal.text = "Total: ${cart.total}"
                binding.tvCartTotalProducts.text = "Total Quantity: ${cart.totalQuantity}"
                val productAdapter = OrderProductAdapter(cart.products) { product ->
                    val action = OrdersFragmentDirections.actionOrdersFragmentToDetailFragment(product.id)
                    fragment.findNavController().navigate(action)
                }
                binding.rvCartProducts.adapter = productAdapter
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRowHolder {
        val binding = OrderRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartRowHolder(binding)
    }

    override fun getItemCount(): Int {
        return cart.size
    }

    override fun onBindViewHolder(holder: CartRowHolder, position: Int) {
        holder.bind(cart[position])
    }
    fun updateCart(newCart: List<Cart>) {
        cart = newCart
        notifyDataSetChanged()
    }
}