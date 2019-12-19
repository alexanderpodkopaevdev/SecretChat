package com.example.secretchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : AppCompatActivity() {

    lateinit var usersDBReference: DatabaseReference
    lateinit var usersChildEventListener: ChildEventListener
    lateinit var userAdapter: UserAdapter
    var users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        attachUserDBReferenceListener()
        buildRecyclerView()


    }

    private fun attachUserDBReferenceListener() {
        usersDBReference = FirebaseDatabase.getInstance().getReference("users")

            usersChildEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue(User::class.java)
                    if (user != null) {
                        users.add(user)
                        userAdapter.notifyDataSetChanged()
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }

            }

        usersDBReference.addChildEventListener(usersChildEventListener)
    }

    private fun buildRecyclerView() {
        userAdapter = UserAdapter(users)
        rvUsers.setHasFixedSize(true)
        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = userAdapter
    }
}
