package com.llc.myinventory.add_inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.llc.myinventory.R
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.databinding.FragmentAddInventoryBinding

class AddInventoryFragment : Fragment() {

    private var _binding: FragmentAddInventoryBinding? = null
    val binding get() = _binding!!

    private val viewModel: AddInventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddInventoryBinding.inflate(inflater, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appDatabase = InventoryItemRoomDatabase.getDatabase(requireContext())

        binding.saveAction.setOnClickListener {
            if (isEntryValid()) {
                viewModel.addInventory(
                    appDatabase = appDatabase,
                    itemName = binding.edtItemName.text.toString(),
                    itemPrice = binding.edtItemPrice.text.toString(),
                    quantityInStock = binding.edtItemQuantity.text.toString()
                )
                val action = AddInventoryFragmentDirections.actionAddInventoryFragmentToInventoryListFragment()
                findNavController().navigate(action)


            }
        }

        viewModel.inputUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is InputInentoryEvent.Success -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                   // findNavController().navigateUp()
                }
                is InputInentoryEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}