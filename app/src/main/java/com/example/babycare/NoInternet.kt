package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.babycare.databinding.ActivityNoInternetBinding
import com.google.firebase.auth.FirebaseAuth

class NoInternet : AppCompatActivity() {
    private lateinit var binding:ActivityNoInternetBinding
    private lateinit var user:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tryAgain.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fadein,R.anim.so_slide)
            finish()
        }
    }
}