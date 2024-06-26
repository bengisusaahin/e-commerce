package com.bengisusahin.e_commerce.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bengisusahin.e_commerce.data.dataFavorites.FavoriteProducts

// This class is used to create a database with the given entities and access the dao classes
@Database(entities = [FavoriteProducts::class], version = 1)
abstract class FavoriteProductsDB : RoomDatabase() {
    abstract fun favoriteProductDao() : FavoriteProductDao
}