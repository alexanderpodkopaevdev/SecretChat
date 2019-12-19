package com.example.secretchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: SecretMessageAdapter
    private var userName = "Default user"
    private var imageCode = 123
    lateinit var recipientUserId: String
    lateinit var database: FirebaseDatabase
    lateinit var messagesDB: DatabaseReference
    lateinit var messagesChildEventListener: ChildEventListener
    lateinit var usersDB: DatabaseReference
    lateinit var usersChildEventListener: ChildEventListener
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        if (intent != null) {
            recipientUserId = intent.getStringExtra("recipientUserId")
        }
        database = FirebaseDatabase.getInstance()
        messagesDB = database.getReference("messages")
        usersDB = database.getReference("users")

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference("chat_images")

        val user = FirebaseAuth.getInstance().currentUser
        userName = user?.displayName.toString()


        val messages = ArrayList<SecretMessage>()
        adapter = SecretMessageAdapter(this, R.layout.message_item, messages)
        lwMessage.adapter = adapter
        pbLoad.visibility = ProgressBar.INVISIBLE
        etMessage.filters = arrayOf(InputFilter.LengthFilter(500))
        iBtnSendMessage.setOnClickListener {
            if (etMessage.text.isNotEmpty()) {
                val message = SecretMessage(
                    etMessage.text.toString(),
                    userName,
                    null,
                    FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    recipientUserId
                )
                etMessage.setText("")
                messagesDB.push().setValue(message)
            } else {
                iBtnSendMessage.isClickable = false
            }
        }
        iBtnSendPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            }
            startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), imageCode)
        }
        etMessage.addTextChangedListener(object : TextWatcher {
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
        messagesChildEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(SecretMessage::class.java)
                if (message?.sender == FirebaseAuth.getInstance().currentUser?.uid.toString()
                    && message.recipient == recipientUserId ||
                    message?.recipient == FirebaseAuth.getInstance().currentUser?.uid.toString()
                    && message.sender == recipientUserId
                )
                    adapter.add(message)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }
        messagesDB.addChildEventListener(messagesChildEventListener)

        usersChildEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)
                if (user?.id == FirebaseAuth.getInstance().currentUser?.uid) {
                    userName = user?.name.toString()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }
        usersDB.addChildEventListener(usersChildEventListener)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.signOut -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                startActivity(Intent(this@ChatActivity, SignInActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imageCode && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                if (selectedImageUri != null) {
                    val imageReference = storageReference.child(selectedImageUri.lastPathSegment!!)
                    val uploadTask = imageReference.putFile(selectedImageUri)

                    val urlTask = uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        imageReference.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            val message = SecretMessage(
                                null, userName, downloadUri.toString(),
                                FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                recipientUserId
                            )
                            messagesDB.push().setValue(message)
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                }
            }
        }
    }
}
