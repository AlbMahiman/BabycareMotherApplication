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
    private lateinit var binding:ActivityDashboardBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        readData()

        binding.pragnancyLayout.setOnClickListener{
            val intent = Intent(this,Pregnancies::class.java)
            startActivity(intent)
        }
        binding.babyLayout.setOnClickListener{
            val intent = Intent(this,Babies::class.java)
            startActivity(intent)
        }
        binding.qrLayout.setOnClickListener{
            val intent = Intent(this,Qr::class.java)
            startActivity(intent)
        }
        binding.contact.setOnClickListener{
            user.signOut()
            // Redirect the user to the login activity or another appropriate destination
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        binding.chat.setOnClickListener{
            val intent = Intent(this,Chat::class.java)
            startActivity(intent)
        }

    }

    private fun readData(){

        binding.loaderLayout.visibility = View.VISIBLE
        binding.dataLayout.visibility = View.GONE

        FirebaseDatabase.getInstance().getReference("Mother").child(user.uid.toString()).get().addOnSuccessListener {
            if(it.exists()){

                binding.topName.text = it.child("motherName").value.toString()
                binding.fullName.text = it.child("motherFullName").value.toString()
                binding.email.text = it.child("email").value.toString()
                binding.address.text = it.child("address").value.toString()

                binding.loaderLayout.visibility = View.GONE
                binding.dataLayout.visibility = View.VISIBLE
            }else{
                val intent = Intent(this,Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}