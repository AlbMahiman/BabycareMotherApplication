package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.babycare.databinding.ActivityViewMotherClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// This is the ViewMotherClinic activity, which displays information about a mother's clinic visit.

class ViewMotherClinic : AppCompatActivity() {
    private lateinit var binding: ActivityViewMotherClinicBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase Authentication.
        binding = ActivityViewMotherClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the clinic ID passed from the previous activity.
        val clinicId = intent.getStringExtra("clinicId")

        // Load data for the clinic visit.
        readData(clinicId.toString())
    }

    private fun readData(clinicId: String) {
        // Hide data layout and show loading spinner.
        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        // Read clinic information from the Firebase Realtime Database.
        FirebaseDatabase.getInstance().getReference("MotherClinic").child(clinicId).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    // Populate the UI with clinic information.
                    binding.purpose.text = it.child("purpose").value.toString()
                    binding.clinicDate.text = it.child("clinicDate").value.toString()
                    binding.createdDate.text = it.child("date").value.toString()

                    // Query MotherConservations related to this clinic visit.
                    FirebaseDatabase.getInstance().getReference("MotherConservations")
                        .orderByChild("clinicId")
                        .equalTo(clinicId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (childSnapshot in snapshot.children) {
                                        // Populate the UI with mother conservation information.
                                        binding.date.text = childSnapshot.child("currentDate").value.toString()
                                        binding.heartBeat.text = childSnapshot.child("heartBeat").value.toString()
                                        binding.position.text = childSnapshot.child("position").value.toString()
                                        binding.pressure.text = childSnapshot.child("pressure").value.toString()
                                        binding.swelling.text = childSnapshot.child("swelling").value.toString()
                                        binding.thriposha.text = childSnapshot.child("thriposha").value.toString()
                                        binding.week.text = childSnapshot.child("week").value.toString()
                                        binding.weight.text = childSnapshot.child("weight").value.toString()

                                        // Show data layout and hide loading spinner.
                                        binding.dataLayout.visibility = View.VISIBLE
                                        binding.loaderLayout.visibility = View.GONE
                                        break
                                    }
                                } else {
                                    // Handle the case where no mother conservation data is found.
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle database error (not commented here).
                            }
                        })
                }
            }
    }
}
