package com.example.babycare

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.babycare.databinding.ActivityQrBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class Qr : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQrBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{

        }

        readData()

    }

    private fun readData(){
        binding.loaderLayout.visibility = View.VISIBLE
        binding.dataLayout.visibility = View.GONE

        val imageName = user.uid.toString()
        val storageReference = FirebaseStorage.getInstance().reference.child("qr_codes/$imageName.png")
        val localFile = File.createTempFile("tempImage","png")
        storageReference.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.qrCodeImage.setImageBitmap(bitmap)
            binding.loaderLayout.visibility = View.GONE
            binding.dataLayout.visibility = View.VISIBLE

        }.addOnFailureListener{
            Toast.makeText(this, "QR Loading Failed", Toast.LENGTH_SHORT).show()
        }

        FirebaseDatabase.getInstance().getReference("Mother").child(user.uid.toString()).get().addOnSuccessListener {
            if(it.exists()){
                binding.topName.text = it.child("motherFullName").value.toString()
            }
        }
    }

}