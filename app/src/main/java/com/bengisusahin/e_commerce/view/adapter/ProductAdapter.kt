package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.databinding.ProductRecyclerRowBinding
import com.bengisusahin.e_commerce.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductAdapter(
    private val productList: List<Product>,
    private val listener: Listener,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ProductRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
         fun bind(product: Product) {
            binding.apply {
                textViewProductName.text = product.title
                textViewProductPrice.text = product.price.toString() + " ₺"
                Glide.with(itemView.context).load(product.images[0]).into(imageViewProduct)

                // Check if the product is in favorites
                viewModel.viewModelScope.launch {
                    val isFavorite = viewModel.isFavorite(product.id)
                    checkBoxFavorite.isChecked = isFavorite
                }

                checkBoxFavorite.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // If the checkbox is now checked, set the favorite icon and add the product to favorites
                        checkBoxFavorite.setButtonDrawable(R.drawable.ic_like_filled)
                        viewModel.insertFavoriteProduct(product)
                    } else {
                        // If the checkbox is now unchecked, set the not favorite icon and remove the product from favorites
                        checkBoxFavorite.setButtonDrawable(R.drawable.ic_like)
                        //viewModel.deleteFavoriteProduct(product)
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
    }
}