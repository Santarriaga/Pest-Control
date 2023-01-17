package com.grumpy.pestcontrol.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.grumpy.pestcontrol.R
import com.grumpy.pestcontrol.databinding.FragmentJobDetailsBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter


class JobDetailsFragment : Fragment() {

    private val args by navArgs<JobDetailsFragmentArgs>()

    private var _binding : FragmentJobDetailsBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJobDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDetails()
    }

    private fun getDetails(){
        binding.tvClientName.text = args.currentJob.name
        binding.tvClientPhone.text = args.currentJob.phone
        val price = "Total: $${args.currentJob.price}"
        binding.tvPrice.text = price
        binding.tvScheduledTime.text = args.currentJob.time
        binding.tvScheduledDate.text = args.currentJob.date
        binding.tvStreetName.text = args.currentJob.street
        binding.tvCityName.text = args.currentJob.city
        binding.tvStateName.text = args.currentJob.state
        binding.tvZip.text = args.currentJob.zipCode


    }
}