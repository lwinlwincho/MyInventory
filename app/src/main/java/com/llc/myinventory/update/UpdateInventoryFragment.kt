package com.llc.myinventory.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.llc.myinventory.R
import com.llc.myinventory.add_inventory.AddInventoryFragmentDirections
import com.llc.myinventory.add_inventory.AddInventoryViewModel
import com.llc.myinventory.add_inventory.InputInentoryEvent
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.databinding.FragmentAddInventoryBinding
import com.llc.myinventory.databinding.FragmentUpdateInventoryBinding
import com.llc.myinventory.detail_inventory.DetailInventoryEvent
import com.llc.myinventory.extension.getFormattedPrice

class UpdateInventoryFragment : Fragment() {
    private var _binding: FragmentUpdateInventoryBinding? = null
    val binding get() = _binding!!

    private val viewModel: UpdateInventoryViewModel by viewModels()

    private val args: UpdateInventoryFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    //return true if the edit text are not empty
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.edtItemName.text.toString(),
            binding.edtItemPrice.text.toString(),
            binding.edtItemQuantity.text.toString(),
        )
    }

    private fun bind(item: InventoryItemEntity) {
        val price = "%.2f".format(item.itemPrice)
        binding.apply {
            edtItemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            edtItemPrice.setText(price, TextView.BufferType.SPANNABLE)
            edtItemQuantity.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appDatabase = InventoryItemRoomDatabase.getDatabase(requireContext())

        viewModel.updateItem(appDatabase,args.id)

        /*binding.saveAction.setOnClickListener {
            if (isEntryValid()) {
                viewModel.updateItem(
                    appDatabase = appDatabase,
                    itemName = binding.edtItemName.text.toString(),
                    itemPrice = binding.edtItemPrice.text.toString(),
                    quantityInStock = binding.edtItemQuantity.text.toString()
                )
                val action = AddInventoryFragmentDirections.actionAddInventoryFragmentToInventoryListFragment()
                findNavController().navigate(action)
            }
        }
*/
        viewModel.updateUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UpdateInventoryEvent.Loading -> {}
                is UpdateInventoryEvent.Success -> {
                    bind(it.updateInventoryEvent)
                   // Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    // busStopAdapter.submitList(it.busList)
                }
                is UpdateInventoryEvent.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
