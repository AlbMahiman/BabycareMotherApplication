package com.example.babycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.babycare.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class Chat : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var user: FirebaseAuth

    private lateinit var messageArrayList: ArrayList<Message>
    private lateinit var messageRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val motherId = user.uid.toString()

        messageRecyclerView = binding.messageList
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.setHasFixedSize(true)
        messageArrayList = arrayListOf<Message>()

        // Send button click listener
        binding.send.setOnClickListener {
            sendMessage(motherId.toString())
        }

        // Listen for new messages
        listenForMessages(motherId.toString())
    }

    // Listen for new messages
    private fun listenForMessages(motherId: String) {
        FirebaseDatabase.getInstance().reference.child("Conversation/$motherId/Message").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Get the newly added element
                val newElement = dataSnapshot.getValue(Message::class.java)

                // Add the new element to the array
                newElement?.let {
                    messageArrayList.add(it)
                    messageRecyclerView.adapter = MessageAdapter(messageArrayList, this@Chat)

                    // Scroll to the last message
                    binding.messageList.post {
                        val lastItemIndex = messageArrayList.size - 1
                        if (lastItemIndex >= 0) {
                            binding.messageList.smoothScrollBy(0, Integer.MAX_VALUE)
                        }
                    }
                }
            }

            // Handle other child events (changed, removed, moved)
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Handle the updated element
                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // Handle the removed element
                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Handle the moved element
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
                // ...
            }
        })
    }

    // Send a new message
    private fun sendMessage(motherId: String) {
        val midId = user.uid.toString()
        val type = "mother"
        val message = binding.message.text.toString()

        if (message.isNotEmpty()) {
            var messageIndex = ""

            fun addMessage(id: String) {
                val messageId = id
                val calendar = Calendar.getInstance()
                val currentDate = calendar.get(Calendar.DATE)
                val currentMonth = calendar.get(Calendar.MONTH) + 1 // Note: Month starts from 0
                val currentYear = calendar.get(Calendar.YEAR)
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)
                val currentSecond = calendar.get(Calendar.SECOND)

                val time = "$currentHour:$currentMinute:$currentSecond"
                val date = "$currentYear-$currentMonth-$currentDate"
                val messageItem = Message(messageId.toString(), type, message.toString(), time, date, midId)
                FirebaseDatabase.getInstance().getReference("Conversation").child("$motherId/Message/$messageId")
                    .setValue(messageItem).addOnSuccessListener {
                        // Clear the message input field
                        binding.message.text = null
                    }
            }

            // Determine the message index
            FirebaseDatabase.getInstance().getReference("Conversation").child("$motherId/Message").get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        messageIndex = (it.childrenCount + 1).toString()
                        addMessage(messageIndex)
                    } else {
                        messageIndex = "1"
                        addMessage(messageIndex)
                    }
                }
        } else {
            Toast.makeText(this, "Enter a message to send.", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to read messages (not currently used)
    private fun readMessages(motherId: String) {
        messageArrayList.clear()
        FirebaseDatabase.getInstance().getReference("Conversation/$motherId/Message").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (fineSnapshot in snapshot.children) {
                        val messageItem = fineSnapshot.getValue(Message::class.java)
                        messageArrayList.add(messageItem!!)
                    }
                    messageRecyclerView.adapter = MessageAdapter(messageArrayList, this@Chat)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                // ...
            }
        })
    }

    // Function to handle item click (not currently used)
    fun onItemClick(position: Int) {}
}
