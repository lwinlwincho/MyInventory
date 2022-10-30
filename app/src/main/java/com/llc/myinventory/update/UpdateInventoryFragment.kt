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
import com.llc.myinventory.add_inventory.AddInventoryFragmentDirections
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.databinding.FragmentUpdateInventoryBinding

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

        val id = args.id
        val appDatabase = InventoryItemRoomDatabase.getDatabase(requireContext())

        viewModel.showItem(appDatabase, args.id)

        viewModel.showUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UpdateInventoryEvent.Loading -> {}
                is UpdateInventoryEvent.SuccessShow -> {
                    bind(it.updateInventoryEvent)
                    // Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    // busStopAdapter.submitList(it.busList)
                }
                is UpdateInventoryEvent.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.saveAction.setOnClickListener {
            if (isEntryValid()) {
                viewModel.updateItem(
                    id=id,
                    appDatabase = appDatabase,
                    itemName = binding.edtItemName.text.toString(),
                    itemPrice = binding.edtItemPrice.text.toString(),
                    itemQuantity = binding.edtItemQuantity.text.toString()
                )
                val action =
                    UpdateInventoryFragmentDirections.actionUpdateInventoryFragmentToInventoryListFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.updateUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UpdateInventoryEvent.Loading -> {}
                is UpdateInventoryEvent.SuccessUpdate -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is UpdateInventoryEvent.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
