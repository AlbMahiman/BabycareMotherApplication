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
import com.google.firebase.storage.FirebaseStorage
import java.io.File

// This is the QR activity, which displays a QR code and user information.

class Qr : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase Authentication.
        binding = ActivityQrBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set up a click listener for the "Back" button.
        binding.btnBack.setOnClickListener {
            // Handle the back button click action (not implemented here).
        }

        // Load data and QR code.
        readData()
    }

    private fun readData() {
        // Show a loading spinner while fetching data from Firebase.
        binding.loaderLayout.visibility = View.VISIBLE
        binding.dataLayout.visibility = View.GONE

        val imageName = user.uid.toString()
        val storageReference = FirebaseStorage.getInstance().reference.child("qr_codes/$imageName.png")
        val localFile = File.createTempFile("tempImage", "png")
        storageReference.getFile(localFile).addOnSuccessListener {
            // Load the QR code image from Firebase Storage.
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.qrCodeImage.setImageBitmap(bitmap)
            binding.loaderLayout.visibility = View.GONE
            binding.dataLayout.visibility = View.VISIBLE
        }.addOnFailureListener {
            Toast.makeText(this, "QR Loading Failed", Toast.LENGTH_SHORT).show()
        }

        // Read user information from the Firebase Realtime Database.
        FirebaseDatabase.getInstance().getReference("Mother").child(user.uid.toString())
            .get().addOnSuccessListener {
                if (it.exists()) {
                    binding.topName.text = it.child("motherFullName").value.toString()
                }
            }
    }
}
