package com.llc.myinventory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(InventoryItemEntity::class), version = 2)
abstract class InventoryItemRoomDatabase : RoomDatabase() {
    abstract fun inventoryItemDao(): InventoryItemDao

    companion object {
        @Volatile
        private var INSTANCE: InventoryItemRoomDatabase? = null

        fun getDatabase(context: Context): InventoryItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    InventoryItemRoomDatabase::class.java,
                    "inventory_item_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}