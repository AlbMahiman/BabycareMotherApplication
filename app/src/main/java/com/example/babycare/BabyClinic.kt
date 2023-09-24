package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyClinic : AppCompatActivity() {

    // Declare necessary variables and references
    private lateinit var binding: ActivityBabyClinicBinding
    private lateinit var user: FirebaseAuth
    private lateinit var clinicArrayList : ArrayList<BabyClinicItem>
    private lateinit var clinicRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase user instance
        binding = ActivityBabyClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get babyId from the intent
        val babyId = intent.getStringExtra("babyId")

        // Set up button click listener to navigate to BabyUpcomingClinic activity
        binding.btnUpComing.setOnClickListener{
            val intent = Intent(this, BabyUpcomingClinic::class.java).also {
                it.putExtra("babyId", babyId.toString())
            }
            startActivity(intent)
            overridePendingTransition(R.anim.so_slide, R.anim.so_slide)
            finish()
        }

        // Initialize RecyclerView and data list
        clinicRecyclerView = binding.clinicList
        clinicRecyclerView.layoutManager = LinearLayoutManager(this)
        clinicRecyclerView.setHasFixedSize(true)
        clinicArrayList = arrayListOf<BabyClinicItem>()

        // Fetch and display completed clinic data
        readData(babyId.toString())
    }

    private fun readData(babyId:String){
        // Hide clinic list and show loading UI
        binding.clinicList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        // Clear the clinicArrayList
        clinicArrayList.clear()

        // Read clinic data from Firebase Database
        FirebaseDatabase.getInstance().getReference("BabyClinic").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val clinicItem =  fineSnapshot.getValue(BabyClinicItem::class.java)
                            if (clinicItem != null) {
                                if(clinicItem.status.toString() == "completed"){
                                    clinicArrayList.add(clinicItem!!)
                                }
                            }
                        }
                    }
                    // Set up the RecyclerView adapter and show the clinic list
                    clinicRecyclerView.adapter = BabyClinicAdapter(clinicArrayList,this@BabyClinic)
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
        // Handle item click to view clinic details
        val intent = Intent(this, ViewBabyClinic::class.java).also {
            it.putExtra("clinicId", clinicArrayList[position].clinicId.toString())
        }
        startActivity(intent)
    }
}
