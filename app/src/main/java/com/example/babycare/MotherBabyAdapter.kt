package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Create an adapter for the RecyclerView to display a list of babies
class MotherBabyAdapter(private val BabyList: ArrayList<Baby>, private val listener: Babies) : RecyclerView.Adapter<MotherBabyAdapter.BabyViewHolder>() {

    // Create views for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        // Inflate the layout for a single baby item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.baby_element, parent, false)
        return BabyViewHolder(itemView)
    }

    // Bind the data to the views in each item
    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        val currentItem = BabyList[position]
        var currentNumber = position + 1

        // Set the baby's name and date of birth in the TextViews
        holder.babyName.text = currentItem.fullName.toString()
        holder.dateOfBirth.text = currentItem.dob.toString()
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return BabyList.size
    }

    // Create a ViewHolder to hold the views for each baby item
    inner class BabyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val babyName: TextView = itemView.findViewById(R.id.babyName)
        val dateOfBirth: TextView = itemView.findViewById(R.id.dateOfBirth)

        init {
            itemView.setOnClickListener(this)
        }

        // Handle item clicks when an item is clicked
        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // Interface to handle item clicks
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
