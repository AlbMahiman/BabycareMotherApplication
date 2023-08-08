package com.example.babycare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babycare.databinding.ActivityViewBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewBabyClinic : AppCompatActivity() {
    private lateinit var binding:ActivityViewBabyClinicBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewBabyClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val clinicId = intent.getStringExtra("clinicId")

        readData(clinicId.toString())
    }

    private fun readData(clinicId:String){

        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("BabyClinic").child(clinicId).get().addOnSuccessListener {
            if(it.exists()){
                binding.purpose.text = it.child("purpose").value.toString()
                binding.clinicDate.text = it.child("clinicDate").value.toString()
                binding.createdDate.text = it.child("date").value.toString()

            }
        }

        FirebaseDatabase.getInstance().getReference("BabyConservations")
            .orderByChild("clinicId")
            .equalTo(clinicId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            binding.date.text = childSnapshot.child("currentDate").value.toString()
                            binding.heartBeat.text = childSnapshot.child("heartBeat").value.toString()
                            binding.skin.text = childSnapshot.child("skin").value.toString()
                            binding.pressure.text = childSnapshot.child("pressure").value.toString()
                            binding.swelling.text = childSnapshot.child("swelling").value.toString()
                            binding.thriposha.text = childSnapshot.child("thriposha").value.toString()
                            binding.week.text = childSnapshot.child("week").value.toString()
                            binding.weight.text = childSnapshot.child("weight").value.toString()
                            binding.dataLayout.visibility = View.VISIBLE
                            binding.loaderLayout.visibility = View.GONE
                            break
                        }
                    } else {

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // handle error
                }
            })
    }
}