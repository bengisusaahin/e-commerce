package com.bengisusahin.e_commerce.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// Swipe to delete functionality for recycler view
abstract class RecyclerViewSwipe: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
}