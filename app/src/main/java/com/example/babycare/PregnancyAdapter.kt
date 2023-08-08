package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PregnancyAdapter(private val PregnancyList:ArrayList<PregnancyItem>, private val listener: Pregnancies): RecyclerView.Adapter<PregnancyAdapter.PregnancyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PregnancyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pregnancy_element,parent,false)
        return PregnancyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: PregnancyViewHolder, position: Int) {
        val currentItem = PregnancyList[position]
        var currentNumber = position + 1
        holder.pregnancyDate.text = currentItem.date.toString()
        holder.pregnancyNumber.text = "Pregnancy Number: $currentNumber"
    }
    override fun getItemCount(): Int {
        return PregnancyList.size
    }

    inner class PregnancyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val pregnancyDate: TextView = itemView.findViewById(R.id.pregnancyDate)
        val pregnancyNumber: TextView = itemView.findViewById(R.id.pregnancyNumber)

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