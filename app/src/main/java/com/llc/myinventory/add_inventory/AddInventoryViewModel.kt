package com.llc.myinventory.add_inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import kotlinx.coroutines.launch

class AddInventoryViewModel:ViewModel() {

    private var _inputUiEvent = MutableLiveData<InputInentoryEvent>()
    val inputUiEvent: LiveData<InputInentoryEvent> = _inputUiEvent

    fun addInventory(
        appDatabase: InventoryItemRoomDatabase,
        itemName: String,
        itemPrice: String,
        quantityInStock:String
    ) {

        viewModelScope.launch {
            try {
                val entity = InventoryItemEntity(
                    itemName = itemName,
                    itemPrice = itemPrice.toDouble(),
                    quantityInStock = quantityInStock.toInt()
                )
                appDatabase.inventoryItemDao().insert(entity)
                _inputUiEvent.postValue(InputInentoryEvent.Success("Successfully Added!"))
            } catch (e: Exception) {
                _inputUiEvent.postValue(InputInentoryEvent.Failure(e.message.toString()))
            }
        }
    }
}

sealed class InputInentoryEvent {
    data class Success(val message: String) : InputInentoryEvent()
    data class Failure(val message: String) : InputInentoryEvent()
}