package com.llc.myinventory.add_inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.llc.myinventory.databinding.FragmentAddInventoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddInventoryFragment : Fragment() {

    private var _binding: FragmentAddInventoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddInventoryViewModel by viewModels()

   /* private val appDatabase by lazy {
        InventoryRoomDatabase.getDatabase(requireContext())
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.inputUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is InputInventoryEvent.Success -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                is InputInventoryEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.saveAction.setOnClickListener {
            if (isEntryValid()) {
                viewModel.addInventory(
                    itemName = binding.edtItemName.text.toString(),
                    itemPrice = binding.edtItemPrice.text.toString(),
                    quantityInStock = binding.edtItemQuantity.text.toString()
                )
            }
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    //return true if the edit text are not empty
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.edtItemName.text.toString(),
            binding.edtItemPrice.text.toString(),
            binding.edtItemQuantity.text.toString(),
        )
    }
}