package com.grumpy.pestcontrol.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.grumpy.pestcontrol.databinding.CustomJobBinding
import com.grumpy.pestcontrol.fragments.CalendarFragmentDirections
import com.grumpy.pestcontrol.models.Job

class JobAdapter(
    private val data : List<Job>,
    private val updateJobCompleted : (Job) -> Unit,
    private val makePhoneCall : (String) -> Unit,
    private val openMaps : (Job) -> Unit
) : RecyclerView.Adapter<JobAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: CustomJobBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomJobBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentJob = Job(data[position].documentId,data[position].name,data[position].phone,data[position].street,
            data[position].city,data[position].state, data[position].zipCode,data[position].date,
            data[position].time,data[position].price,data[position].completed)

        holder.binding.tvName.text = data[position].name
        holder.binding.tvStreet.text = data[position].street
        holder.binding.tvCity.text = data[position].city
        holder.binding.tvState.text = data[position].state
        holder.binding.tvZipCode.text = data[position].zipCode
        holder.binding.tvTime.text = data[position].time


        //determine if card needs a checkmark
        when(currentJob.completed){
            true-> {
                holder.binding.jobLayout.isChecked = true
            }
            else -> {
                holder.binding.jobLayout.isChecked = false
            }
        }

        //lets you add check to card
        holder.binding.jobLayout.setOnLongClickListener {
            holder.binding.jobLayout.isChecked = !holder.binding.jobLayout.isChecked
            currentJob.completed = holder.binding.jobLayout.isChecked
            updateJobCompleted(currentJob)
            true
        }

        //start the details page
        holder.binding.jobLayout.setOnClickListener {
            val action = CalendarFragmentDirections.actionCalendarFragmentToJobDetailsFragment(currentJob)
            holder.itemView.findNavController().navigate(action)
        }

        //starts the edit job fragment
        holder.binding.btnReschedule.setOnClickListener{
            val action = CalendarFragmentDirections.actionCalendarFragmentToUpdateJobFragment(currentJob)
            holder.itemView.findNavController().navigate(action)
        }

        holder.binding.btnPhone.setOnClickListener {
            currentJob.phone?.let { it1 -> makePhoneCall(it1) }
        }

        holder.binding.btnMap.setOnClickListener {
            openMaps(currentJob)
        }


    }


    override fun getItemCount(): Int {
        return data.size
    }



}