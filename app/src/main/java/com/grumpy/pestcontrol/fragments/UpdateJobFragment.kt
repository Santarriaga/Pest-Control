package com.grumpy.pestcontrol.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.grumpy.pestcontrol.R
import com.grumpy.pestcontrol.databinding.FragmentUpdateJobBinding
import com.grumpy.pestcontrol.models.Job
import com.grumpy.pestcontrol.utils.Resource
import com.grumpy.pestcontrol.viewmodels.UpdateJobViewModel
import com.grumpy.pestcontrol.viewmodels.UpdateJobViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


class UpdateJobFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private val args by navArgs<UpdateJobFragmentArgs>()

    private lateinit var viewModel : UpdateJobViewModel

    private var _binding : FragmentUpdateJobBinding ?= null
    private val binding get() = _binding!!

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateJobBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, UpdateJobViewModelFactory()).get((UpdateJobViewModel::class.java))

        retrieveInfo()
        pickDate()
        pickTime()

        binding.btnUpdate.setOnClickListener {
            val client = binding.edtClientName.text.toString()
            val phone = binding.edtPhone.text.toString()
            val address =  binding.edtAddress.text.toString()
            val city = binding.edtCity.text.toString()
            val state = binding.edtState.text.toString()
            val zip = binding.edtZip.text.toString()
            val date = binding.edtDate.text.toString()
            val time = binding.edtTime.text.toString()
            val price = binding.edtPrice.text.toString()

            val job =  Job(args.currentJob.documentId,client,phone,address,city,state,zip,date,time,price,false)

            CoroutineScope(Dispatchers.Main).launch {
                updateJob(job)
            }
        }
    }

    private suspend fun updateJob(job: Job){
        viewModel.updateEntry(job).collect(){ state ->
            when(state){
                is Resource.Loading ->{
                    showToast("Loading")
                }
                is Resource.Success ->{
                    findNavController().navigate(R.id.action_updateJobFragment_to_calendarFragment)
                }
                is Resource.Failed ->{
                    showToast("Failed: ${state.message}")
                }
            }

        }
    }

    private fun showToast(message : String){
        Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
    }

    private fun retrieveInfo() {
        binding.edtClientName.setText(args.currentJob.name)
        binding.edtPhone.setText(args.currentJob.phone)
        binding.edtAddress.setText(args.currentJob.street)
        binding.edtCity.setText(args.currentJob.city)
        binding.edtState.setText(args.currentJob.state)
        binding.edtZip.setText(args.currentJob.zipCode)
        binding.edtDate.setText(args.currentJob.date)
        binding.edtTime.setText(args.currentJob.time)
        binding.edtPrice.setText(args.currentJob.price)

    }

    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate(){
        binding.btnCalendar.setOnClickListener {
            getDateTimeCalendar()
            activity?.let {  it1->
                DatePickerDialog(it1, this,year,month,day).show()
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        binding.edtDate.setText("$savedMonth/$savedDay/$savedYear")
    }

    private fun pickTime(){

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(6)
            .setMinute(0)
            .setTitleText("Select Appointment time")
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .build()

        binding.btnTime.setOnClickListener {
            picker.show(childFragmentManager,"AddJob")
        }

        picker.addOnPositiveButtonClickListener {
            val newHour = picker.hour
            Log.d("AddJob", newHour.toString())
            val newMin = picker.minute


            if( newHour > 12){
                binding.edtTime.setText("${newHour-12}:$newMin P.M.")
            }else{
                binding.edtTime.setText("$newHour:$newMin A.M.")
            }

        }
    }

}