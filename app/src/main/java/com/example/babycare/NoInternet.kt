package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.babycare.databinding.ActivityNoInternetBinding
import com.google.firebase.auth.FirebaseAuth

// This is the NoInternet activity, which is displayed when there is no internet connection.

class NoInternet : AppCompatActivity() {
    private lateinit var binding: ActivityNoInternetBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase Authentication.
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set up a click listener for the "Try Again" button.
        binding.tryAgain.setOnClickListener {
            // Create an intent to go back to the MainActivity.
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Apply a custom animation for the transition.
            overridePendingTransition(R.anim.fadein, R.anim.so_slide)

            // Finish the current NoInternet activity.
            finish()
        }
    }
}
