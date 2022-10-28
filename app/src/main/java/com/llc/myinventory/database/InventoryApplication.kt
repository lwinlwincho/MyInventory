package com.llc.myinventory.database

import android.app.Application

class InventoryApplication : Application() {
    val database: InventoryItemRoomDatabase by lazy {
        InventoryItemRoomDatabase.getDatabase(this)
    }
}