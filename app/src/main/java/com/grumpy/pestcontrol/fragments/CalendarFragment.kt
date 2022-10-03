package com.grumpy.pestcontrol.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.grumpy.pestcontrol.R
import com.grumpy.pestcontrol.adapters.CalendarAdapter
import com.grumpy.pestcontrol.adapters.JobAdapter
import com.grumpy.pestcontrol.utils.Resource
import com.grumpy.pestcontrol.databinding.FragmentCalendarBinding
import com.grumpy.pestcontrol.models.Job
import com.grumpy.pestcontrol.viewmodels.CalendarViewModel
import com.grumpy.pestcontrol.viewmodels.CalendarViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {


    //binding for fragment
    private  var _binding : FragmentCalendarBinding ?= null
    private val binding get() = _binding!!

    //
    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    //current date
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    //selected date
    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    //all days
    private val dates = ArrayList<Date>()
    //Adapter
    private lateinit var list : List<Job>
    private lateinit var jobAdapter: JobAdapter

    private lateinit var viewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //retrieve viewModel
        //viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
        viewModel = ViewModelProvider(this,CalendarViewModelFactory()).get(CalendarViewModel::class.java)

        //for calendar to look nicer
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.calendarRecyclerView)

        //6 max month that calendar will display
        lastDayInCalendar.add(Calendar.MONTH, 6)

        setupCalendar()

        //setup buttons
        binding.calendarPrevButton.setOnClickListener {
            if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setupCalendar()
                else
                    setupCalendar(changeMonth = cal)
            }
        }

        binding.calendarNextButton.setOnClickListener {
            if (cal.before(lastDayInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setupCalendar(changeMonth = cal)
            }
        }


        //navigate to another fragment
        binding.fabAddJob.setOnClickListener{
            findNavController().navigate(R.id.action_calendarFragment_to_addJobFragment)
        }

        //get all jobs
        CoroutineScope(Dispatchers.Main).launch {
            loadJobs()
        }
    }



    //function retrieves jobs from viewModel
    private suspend fun loadJobs(){

        repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.loadJobs().collect(){state ->
                when(state){
                    is Resource.Loading ->{
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Success ->{
                        binding.progressBar.isVisible = false
                        list = state.data
                        setupRecyclerView()

                    }
                    is Resource.Failed ->{
                        Log.d("CalendarFragment", state.message)
                    }
                }
            }
        }

    }

    private fun setupRecyclerView() = binding.rvJobs.apply {

        jobAdapter = JobAdapter(data = list)
        adapter = jobAdapter
        layoutManager = LinearLayoutManager(activity)
    }


    private fun setupCalendar(changeMonth: Calendar?= null){
        binding.txtCurrentMonth.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        //will update calendar when month is changed
        selectedDay =
            when{
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else ->currentDay
            }
        selectedMonth =
            when{
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }

        selectedYear =
            when{
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        // add dates to array
        var currentPosition  = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH,1)

        while(dates.size < maxDaysInMonth){
            if(monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay){
                currentPosition = dates.size
            }
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }

        //set up recycler view
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        binding.calendarRecyclerView.layoutManager = layoutManager
        val calendarAdapter = activity?.let { CalendarAdapter(it,dates,currentDate,changeMonth) }
        binding.calendarRecyclerView.adapter = calendarAdapter

        when{
            currentPosition > 2 ->
                binding.calendarRecyclerView.scrollToPosition(currentPosition - 3)
                maxDaysInMonth - currentPosition < 2 -> binding.calendarRecyclerView.scrollToPosition(currentPosition)
            else ->
                binding.calendarRecyclerView.scrollToPosition(currentPosition)
        }

        calendarAdapter?.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
            }
        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}