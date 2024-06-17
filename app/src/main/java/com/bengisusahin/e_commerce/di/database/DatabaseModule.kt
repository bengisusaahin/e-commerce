package com.bengisusahin.e_commerce.di.database

import android.content.Context
import androidx.room.Room
import com.bengisusahin.e_commerce.database.FavoriteProductDao
import com.bengisusahin.e_commerce.database.FavoriteProductsDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFavoriteProductsDB(
        @ApplicationContext context: Context
    ): FavoriteProductsDB {
        return Room.databaseBuilder(
            context,
            FavoriteProductsDB::class.java,
            "favorite_products.db"
        ).build()
    }
    @Provides
    fun provideFavoriteProductDao(database: FavoriteProductsDB): FavoriteProductDao {
        return database.favoriteProductDao()
    }
}