package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.MotherCourseItem

class MotherCourseAdapter(private val CourseList: ArrayList<MotherCourseItem>, private val listener: MotherCourse) : RecyclerView.Adapter<MotherCourseAdapter.CourseViewHolder>() {

    // Create a view holder for the item view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_element, parent, false)
        return CourseViewHolder(itemView)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentItem = CourseList[position]
        var currentNumber = position + 1

        // Set the medical date and name for the current item
        holder.medDate.text = "Date : ${currentItem.date.toString()}"
        holder.medName.text = "Course : ${currentItem.medName.toString()}"
        holder.elementNum.text = currentNumber.toString()
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return CourseList.size
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val medDate: TextView = itemView.findViewById(R.id.medDate)
        val medName: TextView = itemView.findViewById(R.id.medName)
        val elementNum: TextView = itemView.findViewById(R.id.elementId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
