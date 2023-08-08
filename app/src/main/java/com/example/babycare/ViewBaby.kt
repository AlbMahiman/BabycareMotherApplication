package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babycare.databinding.ActivityViewBabyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ViewBaby : AppCompatActivity() {
    private lateinit var binding:ActivityViewBabyBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewBabyBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val babyId = intent.getStringExtra("babyId")
        readData(babyId.toString())

        binding.bmi.setOnClickListener{
            var intent = Intent(this,BabyBmi::class.java).also {
                it.putExtra("babyId",babyId.toString())
            }
            startActivity(intent)
        }
        binding.med.setOnClickListener{
            var intent = Intent(this,BabyMed::class.java).also {
                it.putExtra("babyId",babyId.toString())
            }
            startActivity(intent)
        }
        binding.course.setOnClickListener{
            var intent = Intent(this,BabyCourses::class.java).also {
                it.putExtra("babyId",babyId.toString())
            }
            startActivity(intent)
        }
        binding.clinic.setOnClickListener{
            var intent = Intent(this,BabyClinic::class.java).also {
                it.putExtra("babyId",babyId.toString())
            }
            startActivity(intent)
        }
    }

    private fun readData(babyId:String){
        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Baby").child(babyId).get().addOnSuccessListener {
            if(it.exists()){
                binding.fullName.text = it.child("fullName").value.toString()
                binding.dob.text = it.child("dob").value.toString()
                binding.dataLayout.visibility = View.VISIBLE
                binding.loaderLayout.visibility = View.GONE
            }
        }
    }
}