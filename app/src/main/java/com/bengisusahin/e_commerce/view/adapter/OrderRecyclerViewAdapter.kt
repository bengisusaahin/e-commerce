package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.data.dataCart.Cart
import com.bengisusahin.e_commerce.databinding.OrderRecyclerRowBinding
import com.bumptech.glide.Glide

class OrderRecyclerViewAdapter(private var cart : List<Cart>)
    : RecyclerView.Adapter<OrderRecyclerViewAdapter.CartRowHolder>() {

        inner class CartRowHolder(val binding : OrderRecyclerRowBinding ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(cart: Cart){
                binding.cartId.text = "Cart ID: ${cart.id}"
                binding.cartTotal.text = "Total: ${cart.total}"
                binding.cartTotalProducts.text = "Total Quantity: ${cart.totalQuantity}"
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