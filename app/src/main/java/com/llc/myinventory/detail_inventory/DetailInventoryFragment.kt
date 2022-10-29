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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.llc.myinventory.InventoryAdapter
import com.llc.myinventory.database.InventoryItemRoomDatabase
import com.llc.myinventory.databinding.FragmentDetailInventoryBinding
import com.llc.myinventory.inventorylist.InventoryListEvent
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

        val adapter = InventoryAdapter {
            val action = InventoryListFragmentDirections.actionInventoryListFragmentToDetailInventoryFragment(it.id)
            findNavController().navigate(action)
        }

       /* recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //val busStopAdapter = BusStopAdapter {}
        recyclerView.adapter = busStopAdapter*/

        viewModel.getDetail(appDatabase,args.id)
        viewModel.inventoryDetailEvent.observe(viewLifecycleOwner){
            when (it) {
                is InventoryListEvent.Loading -> {}
                is InventoryListEvent.Success -> {
                   // busStopAdapter.submitList(it.busList)
                }
                is InventoryListEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}