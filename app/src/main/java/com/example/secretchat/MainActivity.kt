package com.example.secretchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.ProgressBar import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: SecretMessageAdapter
    private var userName = "Default user"
    lateinit var database : FirebaseDatabase
    lateinit var messagesDB : DatabaseReference
    lateinit var messagesChildEventListener: ChildEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance()
        messagesDB = database.getReference("messages")


        val messages = ArrayList<SecretMessage>()
        adapter = SecretMessageAdapter(this,R.layout.message_item,messages)
        lwMessage.adapter = adapter
        pbLoad.visibility = ProgressBar.INVISIBLE
        etMessage.filters = arrayOf(InputFilter.LengthFilter(500))
        iBtnSendMessage.setOnClickListener {
            if (etMessage.text.isNotEmpty()) {
                val message = SecretMessage(etMessage.text.toString(),userName,null)
                etMessage.setText("")
                messagesDB.push().setValue(message)
            }
            else {
                iBtnSendMessage.isClickable = false
            }
        }
        iBtnSendPhoto.setOnClickListener {

        }
        etMessage.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    iBtnSendMessage.background = getDrawable(R.drawable.ic_send_black_24dp)
                    iBtnSendMessage.isClickable = true
                } else {
                    iBtnSendMessage.background = getDrawable(R.drawable.ic_send_grey_24dp)
                    iBtnSendMessage.isClickable = false
                }
            }
        })
        messagesChildEventListener = object:  ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(SecretMessage::class.java)
                adapter.add(message)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }
        messagesDB.addChildEventListener(messagesChildEventListener)




    }
}
