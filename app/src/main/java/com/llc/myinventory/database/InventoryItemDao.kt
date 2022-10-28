package com.llc.myinventory.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: InventoryItemEntity)

    @Update
    fun update(item: InventoryItemEntity)

    @Delete
    fun delete(item: InventoryItemEntity)

    @Query("SELECT * from inventoryitementity WHERE id = :id")
    fun getByInventoryName(id: Int): List<InventoryItemEntity>

    @Query("SELECT * from inventoryitementity ORDER BY name ASC")
    fun getAllInventory(): List<InventoryItemEntity>
}