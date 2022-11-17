package com.llc.myinventory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [InventoryItemEntity::class], version = 2)
abstract class InventoryRoomDatabase : RoomDatabase() {
    abstract fun inventoryItemDao(): InventoryItemDao

   /* companion object {
        @Volatile
        private var INSTANCE: InventoryRoomDatabase? = null

        fun getDatabase(context: Context): InventoryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    InventoryRoomDatabase::class.java,
                    "inventory_item_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }*/
}