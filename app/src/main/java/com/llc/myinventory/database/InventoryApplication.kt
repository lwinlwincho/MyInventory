package com.llc.myinventory.database

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InventoryApplication : Application() {
   /* val database: InventoryRoomDatabase by lazy {
        InventoryRoomDatabase.getDatabase(this)
    }*/
}