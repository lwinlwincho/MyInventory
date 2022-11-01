package com.llc.myinventory.detail_inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.myinventory.R
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.databinding.FragmentDetailInventoryBinding
import com.llc.myinventory.extension.getFormattedPrice

class DetailInventoryFragment : Fragment() {

    private var _binding: FragmentDetailInventoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailInventoryViewModel by viewModels()

    private val args: DetailInventoryFragmentArgs by navArgs()

    private val appDatabase by lazy {
        InventoryItemRoomDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDetail(appDatabase, args.id)
        viewModel.inventoryDetailEvent.observe(viewLifecycleOwner) {
            when (it) {
                is DetailInventoryEvent.Loading -> {}
                is DetailInventoryEvent.Success -> {
                    bind(it.detailInventory)
                }
                is DetailInventoryEvent.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
                is DetailInventoryEvent.Deleted -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun bind(item: InventoryItemEntity) {
        with(binding) {
            itemName.text = item.itemName
            itemPrice.text = item.getFormattedPrice()
            itemCount.text = item.quantityInStock.toString()
            sellItem.isEnabled = viewModel.isStockAvailable(item)
            sellItem.setOnClickListener {
                val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
                viewModel.sellItem(
                    appDatabase = appDatabase,
                    newItem = newItem
                ) // update database
            }
            editItem.setOnClickListener { editItem(item) }
            deleteItem.setOnClickListener { showConfirmationDialog(item) }
            backArrow.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun editItem(item: InventoryItemEntity) {
        val action = DetailInventoryFragmentDirections
            .actionDetailInventoryFragmentToUpdateInventoryFragment(item.id)
        this.findNavController().navigate(action)
    }

    private fun showConfirmationDialog(item: InventoryItemEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem(item)
            }
            .show()
    }

    private fun deleteItem(item: InventoryItemEntity) {
        viewModel.deleteItem(appDatabase, item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}