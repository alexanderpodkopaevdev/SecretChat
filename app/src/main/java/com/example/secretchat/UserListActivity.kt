package com.example.secretchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
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
                    if (user.id != FirebaseAuth.getInstance().currentUser?.uid)
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
        rvUsers.addItemDecoration(DividerItemDecoration(rvUsers.context,DividerItemDecoration.VERTICAL))
        rvUsers.adapter = userAdapter
        userAdapter.setOnUserClickListener(object : UserAdapter.OnUserClickListener {
            override fun onUserClick(position: Int) {
                goToChat(position)
            }

        })
    }

    private fun goToChat(position: Int) {
        val intent = Intent(this@UserListActivity,ChatActivity::class.java)
        intent.putExtra("recipientUserId",users[position].id)
        intent.putExtra("recipientUserName",users[position].name)
        startActivity(intent)
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
                startActivity(Intent(this@UserListActivity, SignInActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

}
