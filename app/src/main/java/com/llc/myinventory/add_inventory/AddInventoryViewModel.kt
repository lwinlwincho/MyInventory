package com.llc.myinventory.add_inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.database.InventoryItemDao
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryRoomDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddInventoryViewModel @Inject constructor(
    private val inventoryItemDao: InventoryItemDao
    ) : ViewModel() {

    private var _inputUiEvent = MutableLiveData<InputInventoryEvent>()
    val inputUiEvent: LiveData<InputInventoryEvent> = _inputUiEvent

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    fun addInventory(
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
                inventoryItemDao.insert(entity)
                _inputUiEvent.postValue(InputInventoryEvent.Success("Successfully Added!"))
            } catch (e: Exception) {
                _inputUiEvent.postValue(InputInventoryEvent.Failure(e.message.toString()))
            }
        }
    }
}

sealed class InputInventoryEvent {
    data class Success(val message: String) : InputInventoryEvent()
    data class Failure(val message: String) : InputInventoryEvent()
}