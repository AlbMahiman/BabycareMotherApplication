package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityMotherClinicBinding
import com.example.babycare.databinding.ActivityMotherUpcomingClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherUpcomingClinic : AppCompatActivity() {

    private lateinit var binding: ActivityMotherUpcomingClinicBinding
    private lateinit var user: FirebaseAuth

    private lateinit var clinicArrayList : ArrayList<MotherClinicItem>
    private lateinit var clinicRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherUpcomingClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")

        // Button to navigate to completed clinics
        binding.btnCompleted.setOnClickListener{
            var intent = Intent(this,MotherClinic::class.java).also {
                it.putExtra("pregnancyId",pregnancyId.toString())
            }
            startActivity(intent)
            overridePendingTransition(R.anim.so_slide,R.anim.so_slide)
            finish()
        }

        clinicRecyclerView = binding.clinicList
        clinicRecyclerView.layoutManager = LinearLayoutManager(this)
        clinicRecyclerView.setHasFixedSize(true)
        clinicArrayList = arrayListOf<MotherClinicItem>()

        readData(pregnancyId.toString())
    }

    private fun readData(pregnancyId:String){
        binding.clinicList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        // Read data from Firebase Realtime Database
        FirebaseDatabase.getInstance().getReference("MotherClinic").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val clinicItem =  fineSnapshot.getValue(MotherClinicItem::class.java)
                            if (clinicItem != null) {
                                // Check if clinic status is pending
                                if(clinicItem.status.toString() == "pending"){
                                    clinicArrayList.add(clinicItem!!)
                                }
                            }
                        }
                    }
                    clinicRecyclerView.adapter = MotherUpcomingClinicAdapter(clinicArrayList,this@MotherUpcomingClinic)
                    binding.clinicList.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        // Handle item click events if needed
    }
}
