package com.llc.myinventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.databinding.ItemInventoryBinding
import com.llc.myinventory.extension.getFormattedPrice

class InventoryAdapter(
    private val onItemClicked: (InventoryItemEntity) -> Unit
) : ListAdapter<InventoryItemEntity, InventoryAdapter.InventoryViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(
            ItemInventoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class InventoryViewHolder(
        private var binding: ItemInventoryBinding,
        private val onItemClicked: (InventoryItemEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inventoryItemEntity: InventoryItemEntity) {
            with(binding) {
                itemName.text = inventoryItemEntity.itemName
                // itemPrice.text = inventoryItemEntity.itemPrice.toString()
                itemPrice.text = inventoryItemEntity.getFormattedPrice()
                itemQuantity.text = inventoryItemEntity.quantityInStock.toString()
                root.setOnClickListener {
                    onItemClicked(inventoryItemEntity)
                }
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<InventoryItemEntity>() {
            override fun areItemsTheSame(
                oldItem: InventoryItemEntity,
                newItem: InventoryItemEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: InventoryItemEntity,
                newItem: InventoryItemEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
