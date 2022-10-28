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
) : ListAdapter<InventoryItemEntity, InventoryAdapter.BusStopViewHolder>(DiffCallback) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        val viewHolder = BusStopViewHolder(
            InventoryListFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BusStopViewHolder(
        private var binding: InventoryListFragmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(inventoryItemEntity: InventoryItemEntity) {
            binding.itemName.text = inventoryItemEntity.itemName
            binding.itemPrice.text = inventoryItemEntity.itemPrice.toString()
            binding.itemQuantity.text = inventoryItemEntity.quantityInStock.toString()

        }
    }
}
