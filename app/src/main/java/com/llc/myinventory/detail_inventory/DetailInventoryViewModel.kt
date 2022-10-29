package com.llc.myinventory.detail_inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.inventorylist.InventoryListEvent
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailInventoryViewModel:ViewModel() {

    private var _inventoryDetailEvent = MutableLiveData<InventoryListEvent>()
    val inventoryDetailEvent: LiveData<InventoryListEvent> = _inventoryDetailEvent


    fun getDetail(appDatabase: InventoryItemRoomDatabase, id:Int){
        _inventoryDetailEvent.value = InventoryListEvent.Loading

        viewModelScope.launch {
            try {
                //get data from offline database
                val result: List<InventoryItemEntity> = appDatabase.inventoryItemDao().getById(id)
                _inventoryDetailEvent.value = InventoryListEvent.Success(result)
            } catch (e: Exception) {
                _inventoryDetailEvent.value = InventoryListEvent.Failure(e.message.toString())
            }
        }
    }
}