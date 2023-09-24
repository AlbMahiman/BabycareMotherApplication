package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MotherMedAdapter(private val MedList: ArrayList<MotherMedItem>, private val listener: MotherMed) :
    RecyclerView.Adapter<MotherMedAdapter.MedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        // Inflate the item layout and return a ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.med_element, parent, false)
        return MedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        // Bind data to the ViewHolder
        val currentItem = MedList[position]
        var currentNumber = position + 1
        holder.medDate.text = "Date : ${currentItem.date.toString()}"
        holder.medName.text = "Medicine : ${currentItem.medName.toString()}"
        holder.elementNum.text = currentNumber.toString()
        holder.instructions.text = "Instructions : ${currentItem.instruction.toString()}"
    }

    override fun getItemCount(): Int {
        // Return the number of items in the list
        return MedList.size
    }

    inner class MedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Define the views in the item layout
        val medDate: TextView = itemView.findViewById(R.id.medDate)
        val medName: TextView = itemView.findViewById(R.id.medName)
        val elementNum: TextView = itemView.findViewById(R.id.elementId)
        val instructions: TextView = itemView.findViewById(R.id.instructions)

        init {
            // Set an OnClickListener for the item view
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            // Handle item click events
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        // Define an interface for item click events
        fun onItemClick(position: Int)
    }
}
