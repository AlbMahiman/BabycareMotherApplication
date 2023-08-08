package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MotherBabyAdapter(private val BabyList:ArrayList<Baby>, private val listener: Babies): RecyclerView.Adapter<MotherBabyAdapter.BabyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.baby_element,parent,false)
        return BabyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        val currentItem = BabyList[position]
        var currentNumber = position + 1
        holder.babyName.text = currentItem.fullName.toString()
        holder.dateOfBirth.text = currentItem.dob.toString()
    }
    override fun getItemCount(): Int {
        return BabyList.size
    }

    inner class BabyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val babyName: TextView = itemView.findViewById(R.id.babyName)
        val dateOfBirth:TextView = itemView.findViewById(R.id.dateOfBirth)

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