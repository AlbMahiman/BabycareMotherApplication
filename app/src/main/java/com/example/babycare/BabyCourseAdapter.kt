package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.BabyCourseItem

class BabyCourseAdapter(private val CourseList: ArrayList<BabyCourseItem>, private val listener: BabyCourses) : RecyclerView.Adapter<BabyCourseAdapter.CourseViewHolder>() {

    // Create a view holder for each course item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        // Inflate the layout for each course item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.course_element, parent, false)
        return CourseViewHolder(itemView)
    }

    // Bind data to each course item in the RecyclerView
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentItem = CourseList[position]
        val currentNumber = position + 1
        holder.medDate.text = currentItem.date.toString()
        holder.medName.text = currentItem.courseName.toString()
        holder.elementNum.text = currentNumber.toString()
    }

    // Return the total number of course items in the RecyclerView
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
                // Handle item click through the listener
                listener.onItemClick(position)
            }
        }
    }

    // Interface to handle course item clicks
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
