package com.example.secretchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        auth = FirebaseAuth.getInstance()
        btnAuthSignUp.setOnClickListener {
            if (etAuthEmail.text.trim().toString().isEmpty()) {
                Toast.makeText(this@SignInActivity, "Введите e-mail", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (etAuthPassword.text.trim().toString().isEmpty()) {
                Toast.makeText(this@SignInActivity, "Введите пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (isLogin) {
                loginAccount(etAuthEmail.text.trim().toString(),etAuthPassword.text.trim().toString())
            } else {
                if (etAuthName.text.trim().toString().isEmpty()) {
                    Toast.makeText(this@SignInActivity, "Введите имя", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (etAuthPasswordSecond.text.trim().toString().isEmpty()) {
                    Toast.makeText(this@SignInActivity,"Введите проверочный пароль",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (etAuthPassword.text.toString() == etAuthPasswordSecond.text.toString()) {
                    createAccount(
                        etAuthName.text.trim().toString(),
                        etAuthEmail.text.trim().toString(),
                        etAuthPassword.text.trim().toString()
                    )
                } else {
                    Toast.makeText(this@SignInActivity, "Пароли не совпадают", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        tvAuthToggleLoginSign.setOnClickListener {
            if (isLogin) {
                isLogin = false
                etAuthName.visibility = View.VISIBLE
                etAuthPasswordSecond.visibility = View.VISIBLE
                btnAuthSignUp.text = getString(R.string.sign_up)
                tvAuthToggleLoginSign.text = getString(R.string.text_login)
            } else {
                isLogin = true
                etAuthName.visibility = View.GONE
                etAuthPasswordSecond.visibility = View.GONE
                btnAuthSignUp.text = getString(R.string.login_in)
                tvAuthToggleLoginSign.text = getString(R.string.text_sign_up)

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }


    private fun createAccount(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }

                // ...
            }
    }
    private fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }


            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}
