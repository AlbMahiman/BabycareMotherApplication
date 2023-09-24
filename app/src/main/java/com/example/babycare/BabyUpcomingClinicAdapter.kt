package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BabyUpcomingClinicAdapter(private val ClinicList: ArrayList<BabyClinicItem>, private val listener: BabyUpcomingClinic) : RecyclerView.Adapter<BabyUpcomingClinicAdapter.BabyUpcomingClinicViewHolder>() {

    // Create a view holder for each upcoming clinic item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyUpcomingClinicViewHolder {
        // Inflate the layout for each upcoming clinic item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mother_clinic_element, parent, false)
        return BabyUpcomingClinicViewHolder(itemView)
    }

    // Bind data to each upcoming clinic item in the RecyclerView
    override fun onBindViewHolder(holder: BabyUpcomingClinicViewHolder, position: Int) {
        val currentItem = ClinicList[position]
        holder.clinicDate.text = currentItem.clinicDate.toString()
        holder.clinicName.text = currentItem.purpose.toString()
        holder.createdDate.text = currentItem.date.toString()

        if (currentItem.status == "pending") {
            holder.statusPending.visibility = View.VISIBLE
            holder.statusCompleted.visibility = View.GONE
        } else if (currentItem.status == "completed") {
            holder.statusPending.visibility = View.GONE
            holder.statusCompleted.visibility = View.VISIBLE
        }
    }

    // Return the total number of upcoming clinic items in the RecyclerView
    override fun getItemCount(): Int {
        return ClinicList.size
    }

    inner class BabyUpcomingClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val clinicDate: TextView = itemView.findViewById(R.id.clinicDate)
        val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        val clinicName: TextView = itemView.findViewById(R.id.purpose)

        val statusPending: FrameLayout = itemView.findViewById(R.id.statusPending)
        val statusCompleted: FrameLayout = itemView.findViewById(R.id.statusCompleted)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Handle item click through the listener
                listener.onItemClick(position)
            }
        }
    }

    // Interface to handle upcoming clinic item clicks
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
