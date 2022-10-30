package com.llc.myinventory.extension

import com.llc.myinventory.database.InventoryItemEntity
import java.text.NumberFormat

fun InventoryItemEntity.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemPrice)