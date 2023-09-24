package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babycare.databinding.ActivityViewBabyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// This is the ViewBaby activity, which displays information about a baby.

class ViewBaby : AppCompatActivity() {
    private lateinit var binding: ActivityViewBabyBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase Authentication.
        binding = ActivityViewBabyBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the baby ID passed from the previous activity.
        val babyId = intent.getStringExtra("babyId")

        // Load data for the baby.
        readData(babyId.toString())

        // Set up click listeners for various actions.
        binding.bmi.setOnClickListener {
            val intent = Intent(this, BabyBmi::class.java).also {
                it.putExtra("babyId", babyId.toString())
            }
            startActivity(intent)
        }

        binding.med.setOnClickListener {
            val intent = Intent(this, BabyMed::class.java).also {
                it.putExtra("babyId", babyId.toString())
            }
            startActivity(intent)
        }

        binding.course.setOnClickListener {
            val intent = Intent(this, BabyCourses::class.java).also {
                it.putExtra("babyId", babyId.toString())
            }
            startActivity(intent)
        }

        binding.clinic.setOnClickListener {
            val intent = Intent(this, BabyClinic::class.java).also {
                it.putExtra("babyId", babyId.toString())
            }
            startActivity(intent)
        }
    }

    private fun readData(babyId: String) {
        // Hide data layout and show loading spinner.
        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        // Read baby information from the Firebase Realtime Database.
        FirebaseDatabase.getInstance().getReference("Baby").child(babyId).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    // Populate the UI with baby information.
                    binding.fullName.text = it.child("fullName").value.toString()
                    binding.dob.text = it.child("dob").value.toString()

                    // Show data layout and hide loading spinner.
                    binding.dataLayout.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }
    }
}
