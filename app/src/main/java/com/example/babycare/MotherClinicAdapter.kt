package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MotherClinicAdapter(private val ClinicList: ArrayList<MotherClinicItem>, private val listener: MotherClinic) : RecyclerView.Adapter<MotherClinicAdapter.ClinicViewHolder>() {

    // Create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        // Inflate the layout for each clinic item view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mother_clinic_element, parent, false)
        return ClinicViewHolder(itemView)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        // Get the current clinic item from the list
        val currentItem = ClinicList[position]

        // Set data to the views in the view holder
        holder.clinicDate.text = currentItem.clinicDate.toString()
        holder.clinicName.text = currentItem.purpose.toString()
        holder.createdDate.text = currentItem.date.toString()

        // Show/hide status indicators based on the clinic status
        if (currentItem.status == "pending") {
            holder.statusPending.visibility = View.VISIBLE
            holder.statusCompleted.visibility = View.GONE
        } else if (currentItem.status == "completed") {
            holder.statusPending.visibility = View.GONE
            holder.statusCompleted.visibility = View.VISIBLE
        }
    }

    // Get the number of items in the list
    override fun getItemCount(): Int {
        return ClinicList.size
    }

    // Define the ClinicViewHolder class
    inner class ClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val clinicDate: TextView = itemView.findViewById(R.id.clinicDate)
        val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        val clinicName: TextView = itemView.findViewById(R.id.purpose)

        val statusPending: FrameLayout = itemView.findViewById(R.id.statusPending)
        val statusCompleted: FrameLayout = itemView.findViewById(R.id.statusCompleted)

        init {
            // Set an item click listener for the view holder
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            // Handle item click event
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // Define an interface for item click events
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
