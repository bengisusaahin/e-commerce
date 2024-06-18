package com.bengisusahin.e_commerce.util

// ProductExtensions.kt
import com.bengisusahin.e_commerce.data.Product
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts

fun Product.toFavoriteProduct(userId: Long): FavoriteProducts {
    return FavoriteProducts(
        fid = 0,
        uid = userId,
        pid = this.id,
        title = this.title,
        price = this.price,
        image = this.images[0],
        description = this.description
    )
}
