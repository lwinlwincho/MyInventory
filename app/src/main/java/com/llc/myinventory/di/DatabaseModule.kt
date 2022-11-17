package com.llc.roomdatabaseeg.di

import android.content.Context
import androidx.room.Room
import com.llc.myinventory.database.InventoryItemDao
import com.llc.myinventory.database.InventoryRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModule {

    @Provides
    @ViewModelScoped
    fun provideInventoryRoomDatabase(@ApplicationContext context: Context): InventoryRoomDatabase {
        return Room.databaseBuilder(
            context,
            InventoryRoomDatabase::class.java,
            "inventory_item_database"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideInventoryDao(inventoryRoomDatabase: InventoryRoomDatabase): InventoryItemDao {
        return inventoryRoomDatabase.inventoryItemDao()
    }
}