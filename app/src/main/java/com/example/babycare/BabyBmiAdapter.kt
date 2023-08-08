package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.BabyBmiItem

class BabyBmiAdapter(private val BmiList:ArrayList<BabyBmiItem>, private val listener: BabyBmi): RecyclerView.Adapter<BabyBmiAdapter.BabyBmiViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyBmiViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bmi_element,parent,false)
        return BabyBmiViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: BabyBmiViewHolder, position: Int) {
        val currentItem = BmiList[position]
        var currentNumber = position + 1
        holder.bmiDate.text = "Date : ${currentItem.date.toString()}"
        holder.bmiVal.text = "BMI : ${currentItem.bmi.toString()}"
        holder.elementId.text = currentNumber.toString()
        holder.bmiStatus.text = "Status : ${currentItem.bmiStatus.toString()}"
    }
    override fun getItemCount(): Int {
        return BmiList.size
    }

    inner class BabyBmiViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val bmiDate: TextView = itemView.findViewById(R.id.bmiDate)
        val bmiVal: TextView = itemView.findViewById(R.id.bmiValue)
        val bmiStatus: TextView = itemView.findViewById(R.id.bmiStatus)
        val elementId: TextView = itemView.findViewById(R.id.elementId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position:Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }


}