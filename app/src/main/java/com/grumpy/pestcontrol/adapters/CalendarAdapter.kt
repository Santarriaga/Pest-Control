package com.grumpy.pestcontrol.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.grumpy.pestcontrol.R
import com.grumpy.pestcontrol.databinding.CustomCalendarDayBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(
    private val context :Context,
    private val data : ArrayList<Date>,
    private val currentDate: Calendar,
    private val changeMonth: Calendar?
) :RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private var mListener: OnItemClickListener? = null
    private var index = -1
    private var selectCurrentDate = true
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]

    private val selectedDay =
        when{
            changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            else -> currentDay
        }
    private val selectedMonth =
        when {
            changeMonth != null -> changeMonth[Calendar.MONTH]
            else -> currentMonth
        }
    private val selectedYear =
        when {
            changeMonth != null -> changeMonth[Calendar.YEAR]
            else -> currentYear
        }


    //viewHolder class
    inner class ViewHolder(val binding : CustomCalendarDayBinding, val listener: OnItemClickListener): RecyclerView.ViewHolder(binding.root) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CustomCalendarDayBinding.inflate(LayoutInflater.from(parent.context),parent,false), mListener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position : Int) {

        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.time = data[holder.adapterPosition]


        val displayMonth = cal[Calendar.MONTH]
        val displayYear= cal[Calendar.YEAR]
        val displayDay = cal[Calendar.DAY_OF_MONTH]

        //set the day
        try {
            val dayInWeek = sdf.parse(cal.time.toString())!!
            sdf.applyPattern("EEE")
            holder.binding.txtDay.text = sdf.format(dayInWeek).toString()
        }catch (e : ParseException){
            Log.v("Exception", e.localizedMessage!!)
        }
        //set the date number
        holder.binding.txtDate.text = cal[Calendar.DAY_OF_MONTH].toString()


        //sets selected
        holder.binding.cardLayout.setOnClickListener {
            index = holder.adapterPosition
            selectCurrentDate = false
            holder.listener.onItemClick(holder.adapterPosition)
            notifyDataSetChanged()
        }

        if (index == holder.adapterPosition)
            makeItemSelected(holder)
        else {
            if (displayDay == selectedDay
                && displayMonth == selectedMonth
                && displayYear == selectedYear
                && selectCurrentDate)
                makeItemSelected(holder)
            else
                makeItemDefault(holder)
        }



    }

    override fun getItemCount(): Int {
        return data.size
    }


    //listener functions
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    private fun makeItemDisabled(holder:ViewHolder){
        holder.binding.txtDate.setTextColor(ContextCompat.getColor(context, R.color.themeColor2))
        holder.binding.txtDay.setTextColor(ContextCompat.getColor(context,R.color.themeColor2))
        holder.binding.cardLayout.setCardBackgroundColor(Color.WHITE)
        holder.binding.cardLayout.isEnabled = false
    }

    private fun makeItemSelected(holder:ViewHolder){
        holder.binding.txtDate.setTextColor(ContextCompat.getColor(context,R.color.themeColor3))
        holder.binding.txtDay.setTextColor(ContextCompat.getColor(context,R.color.themeColor3))
        holder.binding.cardLayout.setCardBackgroundColor(ContextCompat.getColor(context,R.color.themeColor1))
        holder.binding.cardLayout.isEnabled = false
    }

    private fun makeItemDefault(holder : ViewHolder){
        holder.binding.txtDate.setTextColor(Color.BLACK)
        holder.binding.txtDay.setTextColor(Color.BLACK)
        holder.binding.cardLayout.setCardBackgroundColor(Color.WHITE)
        holder.binding.cardLayout.isEnabled = true
    }


}

