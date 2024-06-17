package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.databinding.ProductRecyclerRowBinding
import com.bumptech.glide.Glide

class ProductAdapter(
    private val productList: List<Product>,
    private val listener: Listener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ProductRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                textViewProductName.text = product.title
                textViewProductPrice.text = product.price.toString() + " ₺"
                Glide.with(itemView.context).load(product.images[0]).into(imageViewProduct)

                checkBoxFavorite.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Notify that product is added to favorites
                        listener.onFavoriteClick(product)
                    }
                }

                buttonAddToCart.setOnClickListener {
                    Toast.makeText(it.context, "Added to cart: ${product.title}", Toast.LENGTH_LONG).show()
                    listener.onItemClick(product)
                }

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

    interface Listener {
        fun onItemClick(product: Product)
        fun onFavoriteClick(product: Product)
    }
}