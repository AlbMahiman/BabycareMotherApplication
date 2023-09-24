package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.BabyBmiItem

class BabyBmiAdapter(private val BmiList: ArrayList<BabyBmiItem>, private val listener: BabyBmi) : RecyclerView.Adapter<BabyBmiAdapter.BabyBmiViewHolder>() {

    // Create a view holder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyBmiViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bmi_element, parent, false)
        return BabyBmiViewHolder(itemView)
    }

    // Bind data to each item in the RecyclerView
    override fun onBindViewHolder(holder: BabyBmiViewHolder, position: Int) {
        val currentItem = BmiList[position]
        val currentNumber = position + 1
        holder.bmiDate.text = "Date : ${currentItem.date.toString()}"
        holder.bmiVal.text = "BMI : ${currentItem.bmi.toString()}"
        holder.elementId.text = currentNumber.toString()
        holder.bmiStatus.text = "Status : ${currentItem.bmiStatus.toString()}"
    }

    // Return the total number of items in the RecyclerView
    override fun getItemCount(): Int {
        return BmiList.size
    }

    inner class BabyBmiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val bmiDate: TextView = itemView.findViewById(R.id.bmiDate)
        val bmiVal: TextView = itemView.findViewById(R.id.bmiValue)
        val bmiStatus: TextView = itemView.findViewById(R.id.bmiStatus)
        val elementId: TextView = itemView.findViewById(R.id.elementId)

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

    // Interface to handle item clicks
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
