package com.example.secretchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: SecretMessageAdapter
    private var userName = "Default user"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val messages = ArrayList<SecretMessage>()
        adapter = SecretMessageAdapter(this,R.layout.message_item,messages)
        lwMessage.adapter = adapter
        pbLoad.visibility = ProgressBar.INVISIBLE
        etMessage.filters = arrayOf(InputFilter.LengthFilter(500))
        iBtnSendMessage.setOnClickListener {
            if (etMessage.text.isEmpty())
                iBtnSendMessage.isClickable = false
            else {
                Toast.makeText(this@MainActivity, "Send", Toast.LENGTH_SHORT).show()
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
    }
}
