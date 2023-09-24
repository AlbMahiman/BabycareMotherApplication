package com.example.babycare

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.babycare.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the layout binding and Firebase authentication
        binding = ActivityLoginBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set click listener for the login button
        binding.login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        // Hide login layout and show loading indicator
        binding.loginLayout.visibility = View.GONE
        binding.loaderlayout.visibility = View.VISIBLE

        // Get user input for email and password
        var email = binding.email.text.toString()
        var password = binding.password.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Sign in with email and password
            user.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { mtask ->
                    if (mtask.isSuccessful) {
                        // Check if user data exists in Firebase Realtime Database
                        FirebaseDatabase.getInstance().getReference("Mother")
                            .child(user.uid.toString()).get().addOnSuccessListener {
                                if (it.exists()) {
                                    // If user data exists, navigate to the dashboard
                                    val intent = Intent(this, Dashboard::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.fadein, R.anim.so_slide)
                                    finish()
                                } else {
                                    // If user data doesn't exist, sign out and show an error message
                                    user.signOut()
                                    binding.loginLayout.visibility = View.VISIBLE
                                    binding.loaderlayout.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        "Wrong Patient Account Details",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        val exception = mtask.exception
                        if (exception is FirebaseAuthInvalidCredentialsException) {
                            val errorCode = exception.errorCode
                            if (errorCode == "ERROR_WRONG_PASSWORD") {
                                binding.loginLayout.visibility = View.VISIBLE
                                binding.loaderlayout.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Entered Password is wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (errorCode == "ERROR_INVALID_EMAIL") {
                                binding.loginLayout.visibility = View.VISIBLE
                                binding.loaderlayout.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Entered Email Address is wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                binding.loginLayout.visibility = View.VISIBLE
                                binding.loaderlayout.visibility = View.GONE
                                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                            }
                        } else if (exception is FirebaseAuthInvalidUserException) {
                            val errorCode = exception.errorCode
                            if (errorCode == "ERROR_USER_NOT_FOUND") {
                                // If the email is not registered, check the Realtime Database to find available patient accounts
                                FirebaseDatabase.getInstance().getReference("Mother")
                                    .orderByChild("email")
                                    .equalTo(email)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                for (childSnapshot in snapshot.children) {
                                                    val emailValue =
                                                        childSnapshot.child("email").value.toString()

                                                    if (childSnapshot.child("password").value.toString() == password) {

                                                        user.createUserWithEmailAndPassword(
                                                            email,
                                                            password
                                                        )
                                                            .addOnCompleteListener(MainActivity()) { task ->
                                                                if (task.isSuccessful) {
                                                                    val mother = Mother(
                                                                        childSnapshot.child("motherFullName").value.toString(),
                                                                        childSnapshot.child("motherName").value.toString(),
                                                                        childSnapshot.child("status").value.toString(),
                                                                        childSnapshot.child("numOfBabies").value.toString(),
                                                                        childSnapshot.child("address").value.toString(),
                                                                        childSnapshot.child("dob").value.toString(),
                                                                        childSnapshot.child("nurseId").value.toString(),
                                                                        childSnapshot.child("accountCreatedDate").value.toString(),
                                                                        childSnapshot.child("email").value.toString(),
                                                                        childSnapshot.child("password").value.toString(),
                                                                        user.uid.toString(),
                                                                        childSnapshot.child("phoneNumber").value.toString(),
                                                                        "verified"
                                                                    )
                                                                    val database =
                                                                        FirebaseDatabase.getInstance()
                                                                    val usersRef =
                                                                        database.getReference("Mother/${user.uid}")
                                                                    usersRef.setValue(mother)
                                                                    FirebaseDatabase.getInstance()
                                                                        .getReference("Mother")
                                                                        .child(childSnapshot.key.toString())
                                                                        .removeValue()

                                                                    // Generate the QR code and upload it into Firebase Storage
                                                                    var currentUserIndex =
                                                                        user.uid.toString()

                                                                    val qrCodeData =
                                                                        currentUserIndex
                                                                    val bitMatrix =
                                                                        QRCodeWriter().encode(
                                                                            qrCodeData,
                                                                            BarcodeFormat.QR_CODE,
                                                                            512,
                                                                            512
                                                                        )
                                                                    val width = bitMatrix.width
                                                                    val height = bitMatrix.height
                                                                    val bitmap =
                                                                        Bitmap.createBitmap(
                                                                            width,
                                                                            height,
                                                                            Bitmap.Config.RGB_565
                                                                        )
                                                                    for (x in 0 until width) {
                                                                        for (y in 0 until height) {
                                                                            bitmap.setPixel(
                                                                                x,
                                                                                y,
                                                                                if (bitMatrix.get(
                                                                                        x,
                                                                                        y
                                                                                    )
                                                                                ) Color.BLACK else Color.WHITE
                                                                            )
                                                                        }
                                                                    }

                                                                    // Get a reference to Firebase Storage
                                                                    val storage =
                                                                        FirebaseStorage.getInstance()

                                                                    // Create a reference to the QR code image file in Firebase Storage
                                                                    val qrCodeRef = storage.reference.child(
                                                                        "qr_codes/${currentUserIndex}.png"
                                                                    )

                                                                    // Convert the bitmap to a PNG byte array
                                                                    val baos =
                                                                        ByteArrayOutputStream()
                                                                    bitmap.compress(
                                                                        Bitmap.CompressFormat.PNG,
                                                                        100,
                                                                        baos
                                                                    )
                                                                    val data =
                                                                        baos.toByteArray()

                                                                    // Upload the byte array to Firebase Storage
                                                                    qrCodeRef.putBytes(data)
                                                                        .addOnSuccessListener {
                                                                            // Handle successful upload
                                                                            startActivity(
                                                                                Intent(
                                                                                    this@Login,
                                                                                    Dashboard::class.java
                                                                                )
                                                                            )
                                                                            finish()
                                                                        }
                                                                        .addOnFailureListener { exception ->
                                                                            // Handle failed upload
                                                                        }
                                                                } else {
                                                                    user.signInWithEmailAndPassword(
                                                                        email,
                                                                        password
                                                                    )
                                                                        .addOnCompleteListener { mtask ->
                                                                            if (mtask.isSuccessful) {
                                                                                startActivity(
                                                                                    Intent(
                                                                                        this@Login,
                                                                                        Dashboard::class.java
                                                                                    )
                                                                                )
                                                                            } else {
                                                                                binding.loginLayout.visibility =
                                                                                    View.VISIBLE
                                                                                binding.loaderlayout.visibility =
                                                                                    View.GONE
                                                                                Toast.makeText(
                                                                                    this@Login,
                                                                                    mtask.exception!!.message,
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                        }
                                                                    Toast.makeText(
                                                                        this@Login,
                                                                        task.exception!!.message,
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }

                                                        break

                                                    } else {
                                                        binding.loginLayout.visibility =
                                                            View.VISIBLE
                                                        binding.loaderlayout.visibility =
                                                            View.GONE
                                                        Toast.makeText(
                                                            this@Login,
                                                            "Password is wrong.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        break
                                                    }
                                                }
                                            } else {
                                                // Handle the case where no matching user is found
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            // Handle database error
                                            binding.loginLayout.visibility = View.VISIBLE
                                            binding.loaderlayout.visibility = View.GONE
                                        }
                                    })

                            } else {
                                binding.loginLayout.visibility = View.VISIBLE
                                binding.loaderlayout.visibility = View.GONE
                                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            binding.loginLayout.visibility = View.VISIBLE
                            binding.loaderlayout.visibility = View.GONE
                            Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        } else {
            Toast.makeText(this, "Fill all the input fields", Toast.LENGTH_SHORT).show()
        }
    }
}
