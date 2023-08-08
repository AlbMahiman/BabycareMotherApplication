package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityMotherClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherClinic : AppCompatActivity() {

    private lateinit var binding:ActivityMotherClinicBinding
    private lateinit var user:FirebaseAuth

    private lateinit var clinicArrayList : ArrayList<MotherClinicItem>
    private lateinit var clinicRecyclerView : RecyclerView

    private var pregnancyIndex = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMotherClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")
        pregnancyIndex = pregnancyId.toString()

        binding.btnUpComing.setOnClickListener{
            var intent = Intent(this,MotherUpcomingClinic::class.java).also {
                it.putExtra("pregnancyId",pregnancyId.toString())
            }
            startActivity(intent)
            overridePendingTransition(R.anim.so_slide,R.anim.so_slide)
            finish()
        }

        clinicRecyclerView = binding.clinicList
        clinicRecyclerView.layoutManager = LinearLayoutManager(this)
        clinicRecyclerView.setHasFixedSize(true)
        clinicArrayList = arrayListOf<MotherClinicItem>()

        readData(pregnancyId.toString())
    }

    private fun readData(pregnancyId:String){
        binding.clinicList.visibility = View.GONE
        binding.loaderLayout.visibility = View.VISIBLE

        clinicArrayList.clear()

        FirebaseDatabase.getInstance().getReference("MotherClinic").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val clinicItem =  fineSnapshot.getValue(MotherClinicItem::class.java)
                            if (clinicItem != null) {
                                if(clinicItem.status.toString() == "completed"){
                                    clinicArrayList.add(clinicItem!!)
                                }
                            }
                        }
                    }
                    clinicRecyclerView.adapter = MotherClinicAdapter(clinicArrayList,this@MotherClinic)
                    binding.clinicList.visibility = View.VISIBLE
                    binding.loaderLayout.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {
        var intent = Intent(this,ViewMotherClinic::class.java).also {
            it.putExtra("clinicId",clinicArrayList[position].clinicId.toString())
        }
        startActivity(intent)
    }
}