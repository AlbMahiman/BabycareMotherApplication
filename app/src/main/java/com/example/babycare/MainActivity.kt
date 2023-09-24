package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.babycare.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: FirebaseAuth
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the layout binding and Firebase authentication
        binding = ActivityMainBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Animate the splash logo
        binding.mainSplashLogo.alpha = 0f
        binding.mainSplashLogo.animate().setDuration(1000).alpha(1f)

        if (ConnectionCheck.checkForInternet(this)) {
            // Device is connected to the internet
            handler = Handler()
            handler.postDelayed({
                if (user.currentUser == null) {
                    // If the user is not logged in, navigate to the Login activity
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fadein, R.anim.so_slide)
                    finish()
                } else {
                    // If the user is logged in, navigate to the Dashboard activity
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fadein, R.anim.so_slide)
                    finish()
                }
            }, 3000)
        } else {
            // Device is not connected to the internet
            handler = Handler()
            handler.postDelayed({
                // Navigate to the NoInternet activity
                val intent = Intent(this, NoInternet::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.so_slide, R.anim.so_slide)
                finish()
            }, 3000)
        }
    }
}
