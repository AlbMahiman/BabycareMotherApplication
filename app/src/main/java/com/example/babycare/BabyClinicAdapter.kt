package com.example.babycare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BabyClinicAdapter(private val ClinicList:ArrayList<BabyClinicItem>, private val listener: BabyClinic): RecyclerView.Adapter<BabyClinicAdapter.BabyClinicViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyClinicViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mother_clinic_element,parent,false)
        return BabyClinicViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: BabyClinicViewHolder, position: Int) {
        val currentItem = ClinicList[position]
        var currentNumber = position + 1
        holder.clinicDate.text = currentItem.clinicDate.toString()
        holder.clinicName.text = currentItem.purpose.toString()
        holder.createdDate.text = currentItem.date.toString()

        if(currentItem.status == "pending"){
            holder.statusPending.visibility = View.VISIBLE
            holder.statusCompleted.visibility = View.GONE
        }else if(currentItem.status == "completed"){
            holder.statusPending.visibility = View.GONE
            holder.statusCompleted.visibility = View.VISIBLE
        }

    }
    override fun getItemCount(): Int {
        return ClinicList.size
    }

    inner class BabyClinicViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val clinicDate: TextView = itemView.findViewById(R.id.clinicDate)
        val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        val clinicName: TextView = itemView.findViewById(R.id.purpose)

        val statusPending: FrameLayout = itemView.findViewById(R.id.statusPending)
        val statusCompleted: FrameLayout = itemView.findViewById(R.id.statusCompleted)


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