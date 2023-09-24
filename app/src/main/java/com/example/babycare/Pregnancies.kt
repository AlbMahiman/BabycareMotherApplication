package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityPregnanciesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// This is the Pregnancies activity, which displays a list of pregnancies for a specific user.

class Pregnancies : AppCompatActivity() {

    private lateinit var binding: ActivityPregnanciesBinding
    private lateinit var user: FirebaseAuth

    private lateinit var pregnancyArrayList : ArrayList<PregnancyItem>
    private lateinit var pregnancyRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase Authentication.
        binding = ActivityPregnanciesBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize the RecyclerView and its layout.
        pregnancyRecyclerView = binding.pregnancyList
        pregnancyRecyclerView.layoutManager = LinearLayoutManager(this)
        pregnancyRecyclerView.setHasFixedSize(true)
        pregnancyArrayList = arrayListOf<PregnancyItem>()

        // Load pregnancy data for the current user.
        readData(user.uid.toString())
    }

    private fun readData(motherId:String){
        // Show a loading spinner while fetching data from Firebase.
        binding.loaderLayout.visibility = View.VISIBLE
        binding.pregnancyList.visibility = View.GONE

        // Read pregnancy data from Firebase.
        FirebaseDatabase.getInstance().getReference("Pregnancy").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        // Check if the pregnancy belongs to the current user.
                        if(fineSnapshot.child("motherId").value.toString() == motherId){
                            val pregnancyItem =  fineSnapshot.getValue(PregnancyItem::class.java)
                            pregnancyArrayList.add(pregnancyItem!!)
                        }
                    }

                    // Set up the RecyclerView adapter and hide the loading spinner.
                    pregnancyRecyclerView.adapter = PregnancyAdapter(pregnancyArrayList,this@Pregnancies)
                    binding.loaderLayout.visibility = View.GONE
                    binding.pregnancyList.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur during data retrieval (not commented here).
            }
        })
    }

    // Function to handle item clicks in the RecyclerView.
    fun onItemClick(position: Int) {
        var currentPregnancy = pregnancyArrayList[position]
        var intent = Intent(this, ViewPregnancy::class.java).also {
            it.putExtra("pregnancyId", currentPregnancy.pregnancyId)
        }
        startActivity(intent)
    }
}
