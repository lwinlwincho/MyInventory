package com.llc.myinventory.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: InventoryItemEntity)

    @Query("Update inventoryitementity Set name=:itemName,price=:itemPrice,quantity=:itemQuantity Where id=:id")
    suspend fun update(id: Int, itemName: String, itemPrice: Double, itemQuantity: Int)

    @Query("Update inventoryitementity Set quantity=:itemQuantity Where id=:id")
    suspend fun updateQuantity(id: Int, itemQuantity: Int)

    /*@Update
    suspend fun updateQuantity(item: InventoryItemEntity)
*/
    @Delete
    suspend fun delete(item: InventoryItemEntity)

    @Query("SELECT * from inventoryitementity WHERE id = :id ORDER BY name ASC")
    fun getById(id: Int): InventoryItemEntity

    @Query("SELECT * from inventoryitementity ORDER BY id ASC")
    fun getAllInventory(): List<InventoryItemEntity>
}