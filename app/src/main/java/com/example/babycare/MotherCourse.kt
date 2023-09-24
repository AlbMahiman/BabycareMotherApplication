package com.example.babycare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityMotherCourseBinding
import com.example.midwivesapp.MotherCourseItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherCourse : AppCompatActivity() {
    private lateinit var binding: ActivityMotherCourseBinding
    private lateinit var user: FirebaseAuth

    private lateinit var courseArrayList: ArrayList<MotherCourseItem>
    private lateinit var courseRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherCourseBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the pregnancyId from the intent
        var pregnancyId = intent.getStringExtra("pregnancyId")

        courseRecyclerView = binding.courseList
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.setHasFixedSize(true)
        courseArrayList = arrayListOf<MotherCourseItem>()

        // Call the method to read data from Firebase
        readData(pregnancyId.toString())
    }

    private fun readData(pregnancyId: String) {
        binding.courseList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        // Clear the list before populating with data
        courseArrayList.clear()

        // Read data from the Firebase database
        FirebaseDatabase.getInstance().getReference("MotherCourse").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (fineSnapshot in snapshot.children) {
                        if (fineSnapshot.child("pregnancyId").value.toString() == pregnancyId) {
                            // Get the course item from the database and add it to the list
                            val courseItem = fineSnapshot.getValue(MotherCourseItem::class.java)
                            courseArrayList.add(courseItem!!)
                        }
                    }

                    // Set the adapter for the RecyclerView
                    courseRecyclerView.adapter = MotherCourseAdapter(courseArrayList, this@MotherCourse)

                    // Update the visibility of views
                    binding.courseList.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors in data retrieval here
            }
        })
    }

    fun onItemClick(position: Int) {
        // Handle item click events if needed
    }
}
