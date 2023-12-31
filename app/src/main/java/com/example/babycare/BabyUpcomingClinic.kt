package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityBabyUpcomingClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyUpcomingClinic : AppCompatActivity() {
    private lateinit var binding: ActivityBabyUpcomingClinicBinding
    private lateinit var user: FirebaseAuth

    private lateinit var clinicArrayList : ArrayList<BabyClinicItem>
    private lateinit var clinicRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBabyUpcomingClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get babyId from the intent
        var babyId = intent.getStringExtra("babyId")

        // Initialize UI elements and RecyclerView
        binding.btnCompleted.setOnClickListener {
            var intent = Intent(this, BabyClinic::class.java).also {
                it.putExtra("babyId", babyId.toString())
            }
            startActivity(intent)
            overridePendingTransition(R.anim.so_slide, R.anim.so_slide)
            finish()
        }

        clinicRecyclerView = binding.clinicList
        clinicRecyclerView.layoutManager = LinearLayoutManager(this)
        clinicRecyclerView.setHasFixedSize(true)
        clinicArrayList = arrayListOf<BabyClinicItem>()

        // Fetch and display upcoming clinic data
        readData(babyId.toString())
    }

    private fun readData(babyId:String){
        // Hide clinic list and show loading UI
        binding.clinicList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        clinicArrayList.clear()

        // Read baby clinic data from Firebase Database
        FirebaseDatabase.getInstance().getReference("BabyClinic").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val clinicItem =  fineSnapshot.getValue(BabyClinicItem::class.java)
                            if (clinicItem != null) {
                                if(clinicItem.status.toString() == "pending"){
                                    clinicArrayList.add(clinicItem!!)
                                }
                            }
                        }
                    }
                    // Set up the RecyclerView adapter and show the clinic list
                    clinicRecyclerView.adapter = BabyUpcomingClinicAdapter(clinicArrayList,this@BabyUpcomingClinic)
                    binding.clinicList.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read cancellation if needed
            }
        })
    }

    fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
