package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.databinding.OrderRecyclerRowBinding
import com.bumptech.glide.Glide

class OrderRecyclerView(val productList : List<Product>)
    : RecyclerView.Adapter<OrderRecyclerView.CartRowHolder>() {
    class CartRowHolder(val binding : OrderRecyclerRowBinding ) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRowHolder {
        val binding = OrderRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartRowHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: CartRowHolder, position: Int) {
        holder.binding.cartProductTitle.text = productList[position].title
        holder.binding.cartProductPrice.text = productList[position].price.toString()
        //holder.binding.cartProductCount.text = "Adet : ${productList[position].count}"
        Glide.with(holder.itemView.context).load(productList[position].images).into(holder.binding.cartProductImageView)
    }
}