package com.llc.myinventory.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.detail_inventory.DetailInventoryEvent
import kotlinx.coroutines.launch

class UpdateInventoryViewModel : ViewModel() {

    private var _updateUiEvent = MutableLiveData<UpdateInventoryEvent>()
    val updateUiEvent: LiveData<UpdateInventoryEvent> = _updateUiEvent

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    fun updateItem(appDatabase: InventoryItemRoomDatabase, id: Int) {

        _updateUiEvent.value = UpdateInventoryEvent.Loading

        viewModelScope.launch {
            try {
                //get data from offline database
                val result = appDatabase.inventoryItemDao().getById(id)
                _updateUiEvent.value = UpdateInventoryEvent.Success(result)
            } catch (e: Exception) {
                _updateUiEvent.value = UpdateInventoryEvent.Error(e.message.toString())
            }
        }
    }
}

sealed class UpdateInventoryEvent {
    object Loading : UpdateInventoryEvent()
    data class Success(val detailInventory: InventoryItemEntity) : UpdateInventoryEvent()
    data class Error(val error: String) : UpdateInventoryEvent()

}