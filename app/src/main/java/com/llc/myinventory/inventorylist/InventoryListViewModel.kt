package com.llc.myinventory.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import kotlinx.coroutines.launch
import java.lang.Exception

class InventoryListViewModel : ViewModel() {

    private var _inventoryListEvent = MutableLiveData<InventoryListEvent>()
    val inventoryListEvent: LiveData<InventoryListEvent> = _inventoryListEvent

    fun getAllInventory(appDatabase: InventoryItemRoomDatabase) {
        _inventoryListEvent.value = InventoryListEvent.Loading

        viewModelScope.launch {
            try {
                //get data from offline database
                val result: List<InventoryItemEntity> = appDatabase.inventoryItemDao().getAllInventory()
                _inventoryListEvent.value = InventoryListEvent.Success(result)
            } catch (e: Exception) {
                _inventoryListEvent.value = InventoryListEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class InventoryListEvent {
    data class Success(val inventoryList: List<InventoryItemEntity>) : InventoryListEvent()
    data class Failure(val message: String) : InventoryListEvent()
    object Loading : InventoryListEvent()
}