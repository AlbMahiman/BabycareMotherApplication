package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.babycare.databinding.ActivityViewPregnancyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ViewPregnancy : AppCompatActivity() {
    private lateinit var binding: ActivityViewPregnancyBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewPregnancyBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pregnancyId = intent.getStringExtra("pregnancyId")
        readData(pregnancyId.toString())

        binding.bmi.setOnClickListener{
            var intent = Intent(this,MotherBmi::class.java).also {
                it.putExtra("pregnancyId",pregnancyId.toString())
            }
            startActivity(intent)
        }
        binding.med.setOnClickListener{
            var intent = Intent(this,MotherMed::class.java).also {
                it.putExtra("pregnancyId",pregnancyId.toString())
            }
            startActivity(intent)
        }
        binding.course.setOnClickListener{
            var intent = Intent(this,MotherCourse::class.java).also {
                it.putExtra("pregnancyId",pregnancyId.toString())
            }
            startActivity(intent)
        }
        binding.clinic.setOnClickListener{
            var intent = Intent(this,MotherClinic::class.java).also {
                it.putExtra("pregnancyId",pregnancyId.toString())
            }
            startActivity(intent)
        }


    }

    private fun readData(pregnancyId:String){
        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Pregnancy").child(pregnancyId).get().addOnSuccessListener {
            if(it.exists()){
                binding.date.text = it.child("date").value.toString()
                binding.dataLayout.visibility = View.VISIBLE
                binding.loaderLayout.visibility = View.GONE
            }
        }
    }
}