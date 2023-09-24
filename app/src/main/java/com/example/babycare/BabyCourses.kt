package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityBabyCoursesBinding
import com.example.midwivesapp.BabyCourseItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyCourses : AppCompatActivity() {

    // Declare necessary variables and references
    private lateinit var binding: ActivityBabyCoursesBinding
    private lateinit var user: FirebaseAuth
    private lateinit var courseArrayList : ArrayList<BabyCourseItem>
    private lateinit var courseRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase user instance
        binding = ActivityBabyCoursesBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get babyId from the intent
        val babyId = intent.getStringExtra("babyId")

        // Initialize RecyclerView and data list
        courseRecyclerView = binding.courseList
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.setHasFixedSize(true)
        courseArrayList = arrayListOf<BabyCourseItem>()

        // Fetch and display baby course data
        readData(babyId.toString())
    }

    private fun readData(babyId: String) {
        // Hide course list and show loading UI
        binding.courseList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        // Read baby course data from Firebase Database
        FirebaseDatabase.getInstance().getReference("BabyCourse").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (fineSnapshot in snapshot.children) {
                        if (fineSnapshot.child("babyId").value.toString() == babyId) {
                            val courseItem = fineSnapshot.getValue(BabyCourseItem::class.java)
                            courseArrayList.add(courseItem!!)
                        }
                    }
                    // Set up the RecyclerView adapter and show the course list
                    courseRecyclerView.adapter = BabyCourseAdapter(courseArrayList, this@BabyCourses)
                    binding.courseList.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database read cancellation if needed
            }
        })
    }

    fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
