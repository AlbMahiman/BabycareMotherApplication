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
import com.example.midwivesapp.MotherCourseItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyCourses : AppCompatActivity() {

    private lateinit var binding: ActivityBabyCoursesBinding
    private lateinit var user:FirebaseAuth

    private lateinit var courseArrayList : ArrayList<BabyCourseItem>
    private lateinit var courseRecyclerView : RecyclerView

    private var pregnancyIndex = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBabyCoursesBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")

        courseRecyclerView = binding.courseList
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.setHasFixedSize(true)
        courseArrayList = arrayListOf<BabyCourseItem>()
        readData(babyId.toString())
    }

    private fun readData(babyId:String){
        binding.courseList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("BabyCourse").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val courseItem =  fineSnapshot.getValue(BabyCourseItem::class.java)
                            courseArrayList.add(courseItem!!)
                        }
                    }
                    courseRecyclerView.adapter = BabyCourseAdapter(courseArrayList,this@BabyCourses)
                    binding.courseList.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun onItemClick(position: Int) {

    }
}