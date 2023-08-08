package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityBabyMedBinding
import com.example.midwivesapp.BabyMedItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyMed : AppCompatActivity() {
    private lateinit var binding:ActivityBabyMedBinding
    private lateinit var user: FirebaseAuth

    private lateinit var medicineArrayList : ArrayList<BabyMedItem>
    private lateinit var medicineRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyMedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")

        medicineRecyclerView = binding.medList
        medicineRecyclerView.layoutManager = LinearLayoutManager(this)
        medicineRecyclerView.setHasFixedSize(true)
        medicineArrayList = arrayListOf<BabyMedItem>()
        readData(babyId.toString())
    }
    private fun readData(babyId:String){
        binding.medList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("BabyMed").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val medItem =  fineSnapshot.getValue(BabyMedItem::class.java)
                            medicineArrayList.add(medItem!!)
                        }
                    }
                    medicineRecyclerView.adapter = BabyMedAdapter(medicineArrayList,this@BabyMed)
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