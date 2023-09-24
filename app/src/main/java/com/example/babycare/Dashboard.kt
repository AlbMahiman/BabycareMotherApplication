package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babycare.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the layout binding and Firebase authentication
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Read data from Firebase
        readData()

        // Set click listeners for various buttons
        binding.pragnancyLayout.setOnClickListener {
            val intent = Intent(this, Pregnancies::class.java)
            startActivity(intent)
        }
        binding.babyLayout.setOnClickListener {
            val intent = Intent(this, Babies::class.java)
            startActivity(intent)
        }
        binding.qrLayout.setOnClickListener {
            val intent = Intent(this, Qr::class.java)
            startActivity(intent)
        }
        binding.contact.setOnClickListener {
            // Sign out the user and redirect to the login activity
            user.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        binding.chat.setOnClickListener {
            val intent = Intent(this, Chat::class.java)
            startActivity(intent)
        }
    }

    private fun readData() {
        // Show loading indicator while fetching data
        binding.loaderLayout.visibility = View.VISIBLE
        binding.dataLayout.visibility = View.GONE

        // Read data from the Firebase Realtime Database
        FirebaseDatabase.getInstance().getReference("Mother").child(user.uid.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                // Populate user interface elements with retrieved data
                binding.topName.text = it.child("motherName").value.toString()
                binding.fullName.text = it.child("motherFullName").value.toString()
                binding.email.text = it.child("email").value.toString()
                binding.address.text = it.child("address").value.toString()

                // Hide loading indicator and show data layout
                binding.loaderLayout.visibility = View.GONE
                binding.dataLayout.visibility = View.VISIBLE
            } else {
                // If user data doesn't exist, redirect to the login activity
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
