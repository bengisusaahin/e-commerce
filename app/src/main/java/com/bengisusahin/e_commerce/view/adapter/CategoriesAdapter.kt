package com.bengisusahin.e_commerce.view

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
            // Kategori ismini veya "slug"ını kullanarak resim ID'sini bulun
            val imageId = binding.root.context.resources.getIdentifier("ic_${category.slug.replace('-', '_')}", "drawable", binding.root.context.packageName)

            // Eğer bu ID geçerli bir resim ID'si ise, resmi ayarlayın
            if (imageId != 0) {
                val drawable = ResourcesCompat.getDrawable(binding.root.context.resources, imageId, null)
                drawable?.setBounds(0, 0, 60, 60) // Burada 60x60 bir maksimum boyut belirledik
                binding.categoryName.setCompoundDrawables(null, drawable, null, null)
            } else {
                // Eğer bu ID geçerli bir resim ID'si değilse, varsayılan bir resmi ayarlayın
                val drawable = ResourcesCompat.getDrawable(binding.root.context.resources, R.drawable.ic_category, null)
                drawable?.setBounds(0, 0, 60, 60) // Burada 60x60 bir maksimum boyut belirledik
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
