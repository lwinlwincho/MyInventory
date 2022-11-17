package com.llc.myinventory.detail_inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.database.InventoryItemDao
import com.llc.myinventory.database.InventoryItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class DetailInventoryViewModel @Inject constructor(
    private val inventoryItemDao: InventoryItemDao
) : ViewModel() {

    private var _inventoryDetailEvent = MutableLiveData<DetailInventoryEvent>()
    val inventoryDetailEvent: LiveData<DetailInventoryEvent> = _inventoryDetailEvent

    fun getDetail(id: Int) {

        viewModelScope.launch {
            try {
                //get data from offline database
                val result = inventoryItemDao.getById(id)
                _inventoryDetailEvent.value = DetailInventoryEvent.Success(result)
            } catch (e: Exception) {
                _inventoryDetailEvent.value = DetailInventoryEvent.Error(e.message.toString())
            }
        }
    }

    fun isStockAvailable(item: InventoryItemEntity): Boolean {
        return (item.quantityInStock > 0)
    }

    fun sellItem(newItem: InventoryItemEntity) {
        viewModelScope.launch {
            if (newItem.quantityInStock > 0) {
                try {
                    inventoryItemDao.updateQuantity(newItem.id, newItem.quantityInStock)
                    _inventoryDetailEvent.value = DetailInventoryEvent.Success(newItem)
                } catch (e: Exception) {
                    _inventoryDetailEvent.value = DetailInventoryEvent.Error(e.message.toString())
                }
            }
        }
    }

    fun deleteItem(item: InventoryItemEntity) {
        viewModelScope.launch {
            try {
                inventoryItemDao.delete(item)
            } catch (e: Exception) {
                _inventoryDetailEvent.value = DetailInventoryEvent.Error(e.message.toString())
            }
        }
    }
}

sealed class DetailInventoryEvent {
    object Deleted : DetailInventoryEvent()
    data class Success(val detailInventory: InventoryItemEntity) : DetailInventoryEvent()
    data class Error(val error: String) : DetailInventoryEvent()
}