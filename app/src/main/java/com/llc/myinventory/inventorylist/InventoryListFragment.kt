package com.llc.myinventory.inventorylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.llc.myinventory.InventoryAdapter
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.databinding.InventoryListFragmentBinding

class InventoryListFragment : Fragment() {

    private var _binding: InventoryListFragmentBinding? = null
    val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: InventoryListViewModel by viewModels()

    // create database
    private val appDatabase by lazy {
        InventoryItemRoomDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= InventoryListFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inventoryAdapter = InventoryAdapter {
            val action =
                InventoryListFragmentDirections.actionInventoryListFragmentToDetailInventoryFragment(
                    id = it.id
                )
            view.findNavController().navigate(action)
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter=inventoryAdapter

        viewModel.getAllInventory(appDatabase)
        viewModel.inventoryListEvent.observe(viewLifecycleOwner) {
            when (it) {
                is InventoryListEvent.Loading -> {}
                is InventoryListEvent.Success -> {
                    inventoryAdapter.submitList(it.inventoryList)
                }
                is InventoryListEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action =
                InventoryListFragmentDirections.actionInventoryListFragmentToAddInventoryFragment()
            findNavController().navigate(action)
        }
    }
}