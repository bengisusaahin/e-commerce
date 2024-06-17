package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts
import com.bengisusahin.e_commerce.databinding.FavoriteRecyclerRowBinding
import com.bumptech.glide.Glide

class FavoriteProductsAdapter(
    private var favoriteProducts: List<FavoriteProducts>,
    private val onFavoriteProductClick: ((FavoriteProducts) -> Unit)?
) : RecyclerView.Adapter<FavoriteProductsAdapter.FavoriteProductsViewHolder>(){
    inner class FavoriteProductsViewHolder(val binding: FavoriteRecyclerRowBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bindItem(favoriteProducts: FavoriteProducts){
            binding.favProductTitle.text = favoriteProducts.title
            binding.favProductPrice.text = favoriteProducts.price.toString()
            Glide.with(binding.root).load(favoriteProducts.image).into(binding.ivFavProduct)
        }
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    onFavoriteProductClick?.invoke(favoriteProducts[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductsViewHolder {
        val binding = FavoriteRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favoriteProducts.size
    }

    override fun onBindViewHolder(holder: FavoriteProductsViewHolder, position: Int) {
        holder.bindItem(favoriteProducts[position])
    }

    fun updateFavoriteProducts(newFavoriteProducts: List<FavoriteProducts>){
        this.favoriteProducts = newFavoriteProducts
        notifyDataSetChanged()
    }
}