package com.bengisusahin.e_commerce.view.adapter

import android.util.Log
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.databinding.ProductRecyclerRowBinding

class SearchAdapter(
    private var productList: List<Product>,
    private val listener: Listener
) : RecyclerView.Adapter<SearchAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ProductRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                textViewProductName.text = product.title
                textViewProductPrice.text = product.price.toString() + " ₺"
                Glide.with(itemView.context).load(product.images[0]).into(imageViewProduct)

                root.setOnClickListener {
                    listener.onItemClick(product)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    fun updateData(newProducts: List<Product>) {
        Log.d("SearchAdapter", "Updating data with ${newProducts.size} items")
        this.productList = newProducts
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemClick(product: Product)
    }
}