package com.grumpy.pestcontrol.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.grumpy.pestcontrol.databinding.CustomJobBinding
import com.grumpy.pestcontrol.fragments.CalendarFragmentDirections
import com.grumpy.pestcontrol.models.Job

class JobAdapter(
    private val data : List<Job>,
) : RecyclerView.Adapter<JobAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: CustomJobBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomJobBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvName.text = data[position].name
        holder.binding.tvStreet.text = data[position].street
        holder.binding.tvCity.text = data[position].city
        holder.binding.tvState.text = data[position].state
        holder.binding.tvZipCode.text = data[position].zipCode
        holder.binding.tvTime.text = data[position].time


        //lets you add check to card
        holder.binding.jobLayout.setOnLongClickListener {
            holder.binding.jobLayout.isChecked = !holder.binding.jobLayout.isChecked
            true
        }

        holder.binding.jobLayout.setOnClickListener {
            val currentJob = Job(data[position].name,data[position].phone,data[position].street,
                data[position].city,data[position].state, data[position].zipCode,data[position].date,
                data[position].time,data[position].price,data[position].isCompleted)

            val action = CalendarFragmentDirections.actionCalendarFragmentToJobDetailsFragment(currentJob)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}