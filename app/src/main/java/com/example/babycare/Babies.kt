package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityBabiesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Babies : AppCompatActivity() {

    // Declare necessary variables and references
    private lateinit var binding: ActivityBabiesBinding
    private lateinit var user: FirebaseAuth
    private lateinit var babyArrayList : ArrayList<Baby>
    private lateinit var babyRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase user instance
        binding = ActivityBabiesBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize RecyclerView and data list
        babyRecyclerView = binding.babyList
        babyRecyclerView.layoutManager = LinearLayoutManager(this)
        babyRecyclerView.setHasFixedSize(true)
        babyArrayList = arrayListOf<Baby>()

        // Fetch and display data
        readData(user.uid.toString())
    }

    private fun readData(motherId:String){
        // Show loading UI while fetching data
        binding.loaderLayout.visibility = View.VISIBLE
        binding.babyList.visibility = View.GONE

        // Read data from Firebase Database
        FirebaseDatabase.getInstance().getReference("Baby").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        // Check if the baby belongs to the current mother
                        if(fineSnapshot.child("motherId").value.toString() == motherId){
                            val babyItem =  fineSnapshot.getValue(Baby::class.java)
                            babyArrayList.add(babyItem!!)
                        }
                    }
                    // Hide loading UI and display the RecyclerView
                    binding.loaderLayout.visibility = View.GONE
                    binding.babyList.visibility = View.VISIBLE

                    // Set up the RecyclerView adapter
                    babyRecyclerView.adapter = MotherBabyAdapter(babyArrayList,this@Babies)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle database read cancellation if needed
            }
        })
    }

    fun onItemClick(position: Int) {
        // Handle item click to view baby details
        var currentBaby = babyArrayList[position]
        var intent = Intent(this,ViewBaby::class.java).also {
            it.putExtra("babyId",currentBaby.babyId)
        }
        startActivity(intent)
    }
}
