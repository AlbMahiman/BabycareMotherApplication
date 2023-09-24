package com.example.babycare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityBabyBmiBinding
import com.example.midwivesapp.BabyBmiItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BabyBmi : AppCompatActivity() {

    // Declare necessary variables and references
    private lateinit var binding: ActivityBabyBmiBinding
    private lateinit var user: FirebaseAuth
    private lateinit var bmiArrayList : ArrayList<BabyBmiItem>
    private lateinit var bmiRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the activity layout and Firebase user instance
        binding = ActivityBabyBmiBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get babyId from the intent
        var babyId = intent.getStringExtra("babyId")

        // Initialize RecyclerView and data list
        bmiRecyclerView = binding.bmiList
        bmiRecyclerView.layoutManager = LinearLayoutManager(this)
        bmiRecyclerView.setHasFixedSize(true)
        bmiArrayList = arrayListOf<BabyBmiItem>()

        // Fetch and display BMI data
        readData(babyId.toString())
    }

    private fun readData(babyId:String){
        // Hide data layout and show loading UI
        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE
        bmiArrayList.clear()

        // Read BMI data from Firebase Database
        FirebaseDatabase.getInstance().getReference("BabyBmi").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        // Check if the BMI data belongs to the current baby
                        if(fineSnapshot.child("babyId").value.toString() == babyId){
                            val bmiItem =  fineSnapshot.getValue(BabyBmiItem::class.java)
                            bmiArrayList.add(bmiItem!!)
                        }
                    }
                    // Set up the RecyclerView adapter and show data layout
                    bmiRecyclerView.adapter = BabyBmiAdapter(bmiArrayList,this@BabyBmi)
                    binding.dataLayout.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle database read cancellation if needed
                Toast.makeText(this@BabyBmi, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onItemClick(position: Int) {
        // Handle item click if needed
    }
}
