package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babycare.databinding.ActivityViewPregnancyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// This is the ViewPregnancy activity, which displays information about a pregnancy.

class ViewPregnancy : AppCompatActivity() {
    private lateinit var binding: ActivityViewPregnancyBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase Authentication.
        binding = ActivityViewPregnancyBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the pregnancy ID passed from the previous activity.
        val pregnancyId = intent.getStringExtra("pregnancyId")

        // Load data for the pregnancy.
        readData(pregnancyId.toString())

        // Set up click listeners for various actions.
        binding.bmi.setOnClickListener {
            val intent = Intent(this, MotherBmi::class.java).also {
                it.putExtra("pregnancyId", pregnancyId.toString())
            }
            startActivity(intent)
        }

        binding.med.setOnClickListener {
            val intent = Intent(this, MotherMed::class.java).also {
                it.putExtra("pregnancyId", pregnancyId.toString())
            }
            startActivity(intent)
        }

        binding.course.setOnClickListener {
            val intent = Intent(this, MotherCourse::class.java).also {
                it.putExtra("pregnancyId", pregnancyId.toString())
            }
            startActivity(intent)
        }

        binding.clinic.setOnClickListener {
            val intent = Intent(this, MotherClinic::class.java).also {
                it.putExtra("pregnancyId", pregnancyId.toString())
            }
            startActivity(intent)
        }
    }

    private fun readData(pregnancyId: String) {
        // Hide data layout and show loading spinner.
        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        // Read pregnancy information from the Firebase Realtime Database.
        FirebaseDatabase.getInstance().getReference("Pregnancy").child(pregnancyId).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    // Populate the UI with pregnancy information.
                    binding.date.text = it.child("date").value.toString()

                    // Show data layout and hide loading spinner.
                    binding.dataLayout.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }
    }
}
