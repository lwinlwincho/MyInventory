package com.llc.myinventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.llc.roomdatabaseeg.BusStopAdapter
import com.llc.roomdatabaseeg.bus_full_schedule_list.BusScheduleListEvent
import com.llc.roomdatabaseeg.bus_full_schedule_list.BusScheduleViewModel
import com.llc.roomdatabaseeg.database.schedule.AppDatabase
import com.llc.roomdatabaseeg.databinding.FragmentStopScheduleBinding

class StopScheduleFragment : Fragment() {

    companion object {
        var STOP_NAME = "stopName"
    }

    private var _binding: FragmentStopScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: BusNameViewModel by viewModels()

    private val args: StopScheduleFragmentArgs by navArgs()

    private val appDatabase by lazy {
        AppDatabase.getDatabase(requireContext())
    }

    private val busStopAdapter: BusStopAdapter by lazy {
        BusStopAdapter({})
    }

   // private lateinit var stopName: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStopScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //val busStopAdapter = BusStopAdapter {}
        recyclerView.adapter = busStopAdapter

        viewModel.getByBusName(appDatabase,args.stopName)
        viewModel.busNameEvent.observe(viewLifecycleOwner){
            when (it) {
                is BusScheduleListEvent.Loading -> {}
                is BusScheduleListEvent.Success -> {
                    busStopAdapter.submitList(it.busList)
                }
                is BusScheduleListEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}