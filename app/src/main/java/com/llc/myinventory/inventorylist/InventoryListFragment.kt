package com.llc.myinventory.inventorylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.llc.myinventory.InventoryAdapter
import com.llc.myinventory.database.InventoryRoomDatabase
import com.llc.myinventory.databinding.InventoryListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryListFragment : Fragment() {

    private var _binding: InventoryListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InventoryListViewModel by viewModels()

    // create database
  /*  private val appDatabase by lazy {
        InventoryRoomDatabase.getDatabase(requireContext())
    }
*/
    private val inventoryAdapter by lazy {
        InventoryAdapter {
            val action = InventoryListFragmentDirections
                .actionInventoryListFragmentToDetailInventoryFragment(id = it.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InventoryListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = inventoryAdapter
        }

        viewModel.getAllInventory()
        viewModel.inventoryListEvent.observe(viewLifecycleOwner) {
            when (it) {
                is InventoryListEvent.Success -> {
                    inventoryAdapter.submitList(it.inventoryList)
                }
                is InventoryListEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = InventoryListFragmentDirections
                .actionInventoryListFragmentToAddInventoryFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}