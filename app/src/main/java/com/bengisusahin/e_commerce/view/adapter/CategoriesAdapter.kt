package com.bengisusahin.e_commerce.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.data.dataCategories.CategoriesItem
import com.bengisusahin.e_commerce.databinding.CategoryRecyclerRowBinding

class CategoriesAdapter(private var categories: List<CategoriesItem>) : RecyclerView.Adapter<CategoriesAdapter.CategoryRowHolder>() {

    inner class CategoryRowHolder(val binding: CategoryRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoriesItem) {
            binding.categoryName.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryRowHolder {
        val binding = CategoryRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryRowHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryRowHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<CategoriesItem>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
