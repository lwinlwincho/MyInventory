package com.llc.myinventory.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.add_inventory.InputInentoryEvent
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import kotlinx.coroutines.launch

class UpdateInventoryViewModel : ViewModel() {

    private var _showUiEvent = MutableLiveData<UpdateInventoryEvent>()
    val showUiEvent: LiveData<UpdateInventoryEvent> = _showUiEvent

    private var _updateUiEvent = MutableLiveData<UpdateInventoryEvent>()
    val updateUiEvent: LiveData<UpdateInventoryEvent> = _updateUiEvent

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    fun showItem(appDatabase: InventoryItemRoomDatabase, id: Int) {

        _showUiEvent.value = UpdateInventoryEvent.Loading

        viewModelScope.launch {
            try {
                //get data from offline database
                val result = appDatabase.inventoryItemDao().getById(id)
                _showUiEvent.value = UpdateInventoryEvent.SuccessShow(result)
            } catch (e: Exception) {
                _showUiEvent.value = UpdateInventoryEvent.Error(e.message.toString())
            }
        }
    }

    fun updateItem(
        id:Int,
        appDatabase: InventoryItemRoomDatabase,
        itemName: String,
        itemPrice: String,
        itemQuantity: String
    ) {
        _updateUiEvent.value = UpdateInventoryEvent.Loading

        viewModelScope.launch {
            try {
                val entity = InventoryItemEntity(
                    itemName = itemName,
                    itemPrice = itemPrice.toDouble(),
                    quantityInStock = itemQuantity.toInt()
                )
                appDatabase.inventoryItemDao().update(entity)
                _updateUiEvent.postValue(UpdateInventoryEvent.SuccessUpdate("Successfully Added!"))
            } catch (e: Exception) {
                _updateUiEvent.postValue(UpdateInventoryEvent.Error(e.message.toString()))
            }
        }
    }
}

sealed class UpdateInventoryEvent {
    object Loading : UpdateInventoryEvent()
    data class SuccessShow(val updateInventoryEvent: InventoryItemEntity) : UpdateInventoryEvent()
    data class SuccessUpdate(val message:String) : UpdateInventoryEvent()
    data class Error(val error: String) : UpdateInventoryEvent()

}