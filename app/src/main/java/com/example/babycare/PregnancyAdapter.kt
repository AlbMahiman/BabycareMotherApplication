package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter class for the RecyclerView in the Pregnancies activity.
class PregnancyAdapter(private val pregnancyList: ArrayList<PregnancyItem>, private val listener: Pregnancies) : RecyclerView.Adapter<PregnancyAdapter.PregnancyViewHolder>() {

    // Create a new ViewHolder when needed.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PregnancyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pregnancy_element, parent, false)
        return PregnancyViewHolder(itemView)
    }

    // Bind data to the ViewHolder.
    override fun onBindViewHolder(holder: PregnancyViewHolder, position: Int) {
        val currentItem = pregnancyList[position]
        val currentNumber = position + 1
        holder.pregnancyDate.text = currentItem.date.toString()
        holder.pregnancyNumber.text = "Pregnancy Number: $currentNumber"
    }

    // Return the total number of items in the data set.
    override fun getItemCount(): Int {
        return pregnancyList.size
    }

    // ViewHolder class to hold references to the views in each item.
    inner class PregnancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val pregnancyDate: TextView = itemView.findViewById(R.id.pregnancyDate)
        val pregnancyNumber: TextView = itemView.findViewById(R.id.pregnancyNumber)

        init {
            // Set up a click listener for item clicks.
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Notify the listener when an item is clicked.
                listener.onItemClick(position)
            }
        }
    }

    // Interface to handle item click events.
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
