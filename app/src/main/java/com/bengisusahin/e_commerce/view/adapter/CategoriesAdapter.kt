package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.data.dataCategories.CategoriesItem
import com.bengisusahin.e_commerce.databinding.CategoryRecyclerRowBinding

class CategoriesAdapter(
    private var categories: List<CategoriesItem>,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryRowHolder>() {

    inner class CategoryRowHolder(val binding: CategoryRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoriesItem) {
            binding.categoryName.text = category.name
            binding.categoryName.setOnClickListener {
                onCategoryClick(category.name)
            }
            // Find the image ID for the category
            val imageId = binding.root.context.resources.getIdentifier("ic_${category.slug.replace('-', '_')}", "drawable", binding.root.context.packageName)

            // If the image ID is valid, set the image for the category
            if (imageId != 0) {
                val drawable = ResourcesCompat.getDrawable(binding.root.context.resources, imageId, null)
                drawable?.setBounds(0, 0, 60, 60)
                binding.categoryName.setCompoundDrawables(null, drawable, null, null)
            } else {
                // If the image ID is not valid, set the default category image
                val drawable = ResourcesCompat.getDrawable(binding.root.context.resources, R.drawable.ic_category, null)
                drawable?.setBounds(0, 0, 60, 60)
                binding.categoryName.setCompoundDrawables(null, drawable, null, null)
            }
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
