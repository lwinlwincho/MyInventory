package com.llc.myinventory.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: InventoryItemEntity)

    @Update
    suspend fun update(item: InventoryItemEntity)

    @Delete
    fun delete(item: InventoryItemEntity)

    @Query("SELECT * from inventoryitementity WHERE id = :id ORDER BY name ASC")
    fun getById(id:Int): InventoryItemEntity

    @Query("SELECT * from inventoryitementity ORDER BY name ASC")
    fun getAllInventory(): List<InventoryItemEntity>
}