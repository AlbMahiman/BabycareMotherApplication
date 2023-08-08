package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityMotherMedBinding
import com.example.babycare.databinding.ActivityViewPregnancyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherMed : AppCompatActivity() {
    private lateinit var binding: ActivityMotherMedBinding
    private lateinit var user: FirebaseAuth

    private lateinit var medicineArrayList : ArrayList<MotherMedItem>
    private lateinit var medicineRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherMedBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")

        medicineRecyclerView = binding.medList
        medicineRecyclerView.layoutManager = LinearLayoutManager(this)
        medicineRecyclerView.setHasFixedSize(true)
        medicineArrayList = arrayListOf<MotherMedItem>()
        readData(pregnancyId.toString())
    }

    private fun readData(pregnancyId:String){
        binding.medList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("MotherMed").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val motherMedItem =  fineSnapshot.getValue(MotherMedItem::class.java)
                            medicineArrayList.add(motherMedItem!!)
                        }
                    }
                    medicineRecyclerView.adapter = MotherMedAdapter(medicineArrayList,this@MotherMed)
                    binding.medList.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {

    }

}