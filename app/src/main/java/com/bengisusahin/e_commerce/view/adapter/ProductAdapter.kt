package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.databinding.ProductRecyclerRowBinding
import com.bumptech.glide.Glide

class ProductAdapter(private val productList : List<Product>, private val listener : Listener) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(val binding : ProductRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)  {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.textViewProductName.text = productList[position].title
        holder.binding.textViewProductPrice.text = productList[position].price.toString() + "₺"
        if (productList[position].images.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(productList[position].images[0]).into(holder.binding.imageViewProduct)
        }
        holder.binding.buttonAddToCart.setOnClickListener {
            Toast.makeText(it.context,"Added to cart: ${productList[position].title}",Toast.LENGTH_LONG).show()
            listener.onItemClick(productList[position])
        }
    }

    interface Listener {
        fun onItemClick(product : Product)
    }
}