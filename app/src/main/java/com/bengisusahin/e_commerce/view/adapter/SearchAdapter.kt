package com.bengisusahin.e_commerce.view.adapter

import android.util.Log
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.dataProduct.Product
import com.bengisusahin.e_commerce.databinding.ProductRecyclerRowBinding
import com.bengisusahin.e_commerce.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class SearchAdapter(
    private var productList: List<Product>,
    private val listener: Listener,
    private val homeViewModel: HomeViewModel
) : RecyclerView.Adapter<SearchAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ProductRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                textViewProductName.text = product.title
                textViewProductPrice.text = product.price.toString() + " ₺"
                Glide.with(itemView.context).load(product.images[0]).into(imageViewProduct)

                // Check if the product is in favorites
                homeViewModel.viewModelScope.launch {
                    val isFavorite = homeViewModel.isFavorite(product.id)
                    Log.d("isFavorite", "Is product favorite: $isFavorite")
                    checkBoxFavorite.isChecked = isFavorite
                    Log.d("fav", "bind: $isFavorite")
                }

                checkBoxFavorite.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // If the checkbox is now checked, set the favorite icon and add the product to favorites
                        checkBoxFavorite.setButtonDrawable(R.drawable.ic_like_filled)
                        homeViewModel.insertFavoriteProduct(product)
                    } else {
                        // If the checkbox is now unchecked, set the not favorite icon and remove the product from favorites
                        checkBoxFavorite.setButtonDrawable(R.drawable.ic_like)
                        homeViewModel.viewModelScope.launch {
                            val deleteResult = homeViewModel.deleteFavoriteProduct(product)
                            if (deleteResult == 0) {
                                Log.d("deleteFavoriteProduct", "Failed to delete product from favorites")
                            } else {
                                Log.d("deleteFavoriteProduct", "Product successfully deleted from favorites")
                            }
                        }
                    }
                }

                buttonAddToCart.setOnClickListener {
                    listener.addToCart(product)
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

    fun updateData(newProducts: List<Product>) {
        Log.d("SearchAdapter", "Updating data with ${newProducts.size} items")
        this.productList = newProducts
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemClick(product: Product)
        fun addToCart(product: Product)
    }
}