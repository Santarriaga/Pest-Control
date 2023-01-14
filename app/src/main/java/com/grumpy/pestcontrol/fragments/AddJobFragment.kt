package com.grumpy.pestcontrol.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.grumpy.pestcontrol.BuildConfig.MAPS_API_KEY
import com.grumpy.pestcontrol.R
import com.grumpy.pestcontrol.databinding.FragmentAddJobBinding
import com.grumpy.pestcontrol.models.Job
import com.grumpy.pestcontrol.utils.Resource
import com.grumpy.pestcontrol.viewmodels.AddJobViewModel
import com.grumpy.pestcontrol.viewmodels.AddJobViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class AddJobFragment : Fragment() , DatePickerDialog.OnDateSetListener{

    private lateinit var viewModel : AddJobViewModel

    private var _binding : FragmentAddJobBinding ?= null
    private val binding get() = _binding!!


    //variables
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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddJobBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this,AddJobViewModelFactory()).get(AddJobViewModel::class.java)

        pickDate()
        pickTime()


        binding.btnSave.setOnClickListener {

            if( checkAllFields() ){

                val client = binding.edtClientName.text.toString()
                val phone = binding.edtPhone.text.toString()
                val address =  binding.edtAddress.text.toString()
                val city = binding.edtCity.text.toString()
                val state = binding.edtState.text.toString()
                val zip = binding.edtZip.text.toString()
                val date = binding.edtDate.text.toString()
                val time = binding.edtTime.text.toString()
                val price = binding.edtPrice.text.toString()

                val job =  Job("",client,phone,address,city,state,zip,date,time,price,false)

                CoroutineScope(Dispatchers.Main).launch {
                    addJob(job)
                }
            }else{
                showToast("Missing some inputs")
            }

        }

    }


    private fun checkAllFields() : Boolean {

        var res = true

        if(binding.edtClientName.length() == 0){
            binding.edtClientName.error = "This Field is required!"
            res = false
        }
        if(binding.edtAddress.length() == 0){
            binding.edtAddress.error =  "This Field is required!"
            res = false
        }
        if(binding.edtCity.length() == 0){
            binding.edtCity.error =  "This Field is required!"
            res = false
        }


        if(binding.edtPhone.length() == 0){
            binding.edtPhone.error = "Enter a valid phone number"
            res = false

        }

        if(binding.edtZip.length() == 0){
            binding.edtZip.error =  "This Field is required!"
            res = false

        }else if (binding.edtZip.length() > 5){
            binding.edtZip.error =  "Zip code requires 5 digits"
            res = false
        }

        return res
    }

    private suspend fun addJob(job: Job){
        viewModel.addJob(job).collect(){ state ->
            when(state){
                is Resource.Loading ->{
                    showToast("Loading")
                }
                is Resource.Success ->{
                    findNavController().navigate(R.id.action_addJobFragment_to_calendarFragment)
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

            activity?.let { it1 ->
                DatePickerDialog(it1,this,year,month,day).show()
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
            .setInputMode(INPUT_MODE_KEYBOARD)
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}