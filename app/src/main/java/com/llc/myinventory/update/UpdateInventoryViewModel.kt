package com.llc.myinventory.update

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
class UpdateInventoryViewModel @Inject constructor(
    private val inventoryItemDao: InventoryItemDao
) : ViewModel() {

    private var _updateUiEvent = MutableLiveData<UpdateInventoryEvent>()
    val updateUiEvent: LiveData<UpdateInventoryEvent> = _updateUiEvent

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    fun showItem(id: Int) {

        viewModelScope.launch {
            try {
                //get data from offline database
                val result = inventoryItemDao.getById(id)
                _updateUiEvent.value = UpdateInventoryEvent.SuccessShow(result)
            } catch (e: Exception) {
                _updateUiEvent.value = UpdateInventoryEvent.Error(e.message.toString())
            }
        }
    }

    fun updateItem(
        id: Int,
        itemName: String,
        itemPrice: String,
        quantityInStock: String
    ) {

        viewModelScope.launch {
            try {
                inventoryItemDao.update(id, itemName, itemPrice.toDouble(), quantityInStock.toInt())
                _updateUiEvent.postValue(UpdateInventoryEvent.SuccessUpdate("Success Updated"))
            } catch (e: Exception) {
                _updateUiEvent.postValue(UpdateInventoryEvent.Error(e.message.toString()))
            }
        }
    }
}

sealed class UpdateInventoryEvent {
    data class SuccessShow(val updateInventoryEvent: InventoryItemEntity) : UpdateInventoryEvent()
    data class SuccessUpdate(val message: String) : UpdateInventoryEvent()
    data class Error(val error: String) : UpdateInventoryEvent()

}