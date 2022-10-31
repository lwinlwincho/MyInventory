package com.llc.myinventory.detail_inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import kotlinx.coroutines.launch
import kotlin.Exception

class DetailInventoryViewModel : ViewModel() {

    private var _inventoryDetailEvent = MutableLiveData<DetailInventoryEvent>()
    val inventoryDetailEvent: LiveData<DetailInventoryEvent> = _inventoryDetailEvent

    fun getDetail(appDatabase: InventoryItemRoomDatabase, id: Int) {

        _inventoryDetailEvent.value = DetailInventoryEvent.Loading

        viewModelScope.launch {
            try {
                //get data from offline database
                val result = appDatabase.inventoryItemDao().getById(id)
                _inventoryDetailEvent.value = DetailInventoryEvent.Success(result)
            } catch (e: Exception) {
                _inventoryDetailEvent.value = DetailInventoryEvent.Error(e.message.toString())
            }
        }
    }

    fun isStockAvailable(item: InventoryItemEntity): Boolean {
        return (item.quantityInStock > 0)
    }

    fun sellItem(appDatabase: InventoryItemRoomDatabase,item:InventoryItemEntity) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newQuantity = item.copy(quantityInStock = item.quantityInStock - 1)
            editQuantityItem(appDatabase,item.id,newQuantity)
        }
    }

    private fun editQuantityItem(
        appDatabase: InventoryItemRoomDatabase,
        id:Int,
        item:InventoryItemEntity
    ) {
        try {
            viewModelScope.launch {
                appDatabase.inventoryItemDao().updateQuantity(id,item.quantityInStock)
            }
        } catch (e: Exception) {
            _inventoryDetailEvent.value = DetailInventoryEvent.Error(e.message.toString())
        }
    }

    fun deleteItem(appDatabase: InventoryItemRoomDatabase, item: InventoryItemEntity) {
        try {
            viewModelScope.launch {
                appDatabase.inventoryItemDao().delete(item)
            }
        } catch (e: Exception) {
            _inventoryDetailEvent.value = DetailInventoryEvent.Error(e.message.toString())
        }
    }
}

sealed class DetailInventoryEvent {
    object Loading : DetailInventoryEvent()
    data class Success(val detailInventory: InventoryItemEntity) : DetailInventoryEvent()
    data class Error(val error: String) : DetailInventoryEvent()
}