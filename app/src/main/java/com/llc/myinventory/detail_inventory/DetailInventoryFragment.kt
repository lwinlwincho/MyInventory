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
import com.llc.myinventory.InventoryAdapter
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.databinding.FragmentDetailInventoryBinding
import com.llc.myinventory.extension.getFormattedPrice
import com.llc.myinventory.inventorylist.InventoryListFragmentDirections

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
                    // busStopAdapter.submitList(it.busList)
                }
                is DetailInventoryEvent.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun bind(item: InventoryItemEntity) {
        binding.apply {
            itemName.text = item.itemName
            itemPrice.text = item.getFormattedPrice()
            itemCount.text = item.quantityInStock.toString()
            sellItem.isEnabled = viewModel.isStockAvailable(item)
            sellItem.setOnClickListener { viewModel.sellItem(appDatabase,item) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}