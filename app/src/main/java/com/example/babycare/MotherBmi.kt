package com.example.babycare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityMotherBmiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MotherBmi : AppCompatActivity() {
    private lateinit var binding:ActivityMotherBmiBinding
    private lateinit var user:FirebaseAuth

    private lateinit var bmiArrayList : ArrayList<Bmi>
    private lateinit var bmiRecyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherBmiBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")

        bmiRecyclerView = binding.bmiList
        bmiRecyclerView.layoutManager = LinearLayoutManager(this)
        bmiRecyclerView.setHasFixedSize(true)
        bmiArrayList = arrayListOf<Bmi>()
        readData(pregnancyId.toString())

    }

    private fun readData(pregnancyId:String){
        binding.dataLayout.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE
        bmiArrayList.clear()
        FirebaseDatabase.getInstance().getReference("Bmi").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val bmiItem =  fineSnapshot.getValue(Bmi::class.java)
                            bmiArrayList.add(bmiItem!!)
                        }
                    }
                    bmiRecyclerView.adapter = BmiAdapter(bmiArrayList,this@MotherBmi)
                    binding.dataLayout.visibility = View.VISIBLE
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