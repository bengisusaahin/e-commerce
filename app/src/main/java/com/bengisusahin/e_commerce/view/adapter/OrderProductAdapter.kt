package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.dataCart.Product
import com.bengisusahin.e_commerce.databinding.FavoriteRecyclerRowBinding
import com.bumptech.glide.Glide

// Adapter for the products in the orders recycler view
class OrderProductAdapter(
    private var products: List<Product>,
    private val onProductClick: ((Product) -> Unit)?
) : RecyclerView.Adapter<OrderProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding: FavoriteRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(product: Product) {
            binding.favProductTitle.text = product.title
            binding.favProductPrice.text = product.price.toString()
            Glide.with(binding.root).load(product.thumbnail).into(binding.ivFavProduct)
        }
        // When a product is clicked, navigate to the product detail page
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onProductClick?.invoke(products[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = FavoriteRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindItem(products[position])
    }
}