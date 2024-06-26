package com.bengisusahin.e_commerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bengisusahin.e_commerce.databinding.DetailImageProductBinding
import com.bumptech.glide.Glide

// Adapter for the viewpager in the detail fragment
class DetailImageViewPagerAdapter(private val imageUrlList: List<String>) :
    RecyclerView.Adapter<DetailImageViewPagerAdapter.ViewPagerViewHolder>() {

    override fun getItemCount() = imageUrlList.size

    inner class ViewPagerViewHolder(val binding: DetailImageProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            //binding.detailItemImg.loadImage(imageUrl)
            Glide.with(binding.root.context).load(imageUrl).into(binding.detailItemImg)
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailImageViewPagerAdapter.ViewPagerViewHolder {
        val binding =
            DetailImageProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetailImageViewPagerAdapter.ViewPagerViewHolder,
        position: Int
    ) {
        holder.bind(imageUrlList[position])
    }
}