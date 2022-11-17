package com.llc.myinventory.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.llc.myinventory.database.InventoryItemEntity
import com.llc.myinventory.database.InventoryRoomDatabase
import com.llc.myinventory.databinding.FragmentUpdateInventoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateInventoryFragment : Fragment() {

    private var _binding: FragmentUpdateInventoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpdateInventoryViewModel by viewModels()

    private val args: UpdateInventoryFragmentArgs by navArgs()

   /* private val appDatabase by lazy {
        InventoryRoomDatabase.getDatabase(requireContext())
    }
*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showItem(args.id)

        viewModel.updateUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UpdateInventoryEvent.SuccessShow -> {
                    bind(it.updateInventoryEvent)
                }
                is UpdateInventoryEvent.SuccessUpdate -> {
                    findNavController().navigateUp()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is UpdateInventoryEvent.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bind(item: InventoryItemEntity) {
        val price = "%.2f".format(item.itemPrice)
        with(binding) {
            edtItemName.setText(item.itemName)
            edtItemPrice.setText(price)
            edtItemQuantity.setText(item.quantityInStock.toString())
            saveAction.setOnClickListener { update() }
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

    private fun update() {
        if (isEntryValid()) {
            viewModel.updateItem(
                id = args.id,
                itemName = binding.edtItemName.text.toString(),
                itemPrice = binding.edtItemPrice.text.toString(),
                quantityInStock = binding.edtItemQuantity.text.toString()
            )
        }
    }

}

