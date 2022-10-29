package com.llc.myinventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.databinding.InventoryListFragmentBinding

class InventoryAdapter(
    private val onItemClicked: (InventoryItemEntity) -> Unit
) : ListAdapter<InventoryItemEntity, InventoryAdapter.InventoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(
            InventoryListFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentPosition = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentPosition)
        }
        holder.bind(currentPosition)
    }

    class InventoryViewHolder(
        private var binding: InventoryListFragmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(inventoryItemEntity: InventoryItemEntity) {
            binding.apply {
                itemName.text = inventoryItemEntity.itemName
                itemPrice.text = inventoryItemEntity.itemPrice.toString()
                itemQuantity.text = inventoryItemEntity.quantityInStock.toString()
            }

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<InventoryItemEntity>() {
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
