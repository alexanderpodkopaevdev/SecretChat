package com.example.secretchat

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.message_item.view.*

class SecretMessageAdapter(context: Context, resource: Int, messages: List<SecretMessage>) :
    ArrayAdapter<SecretMessage>(context, resource, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: (context as Activity).layoutInflater.inflate(R.layout.message_item,parent,false)

        view.ivPhoto
        view.tvName
        view.tvText

        val message = getItem(position)
        if (message!= null) {
            view.tvName.text = message.name
            if (message.imageUrl.isNullOrEmpty()) {
                view.tvText.visibility = View.VISIBLE
                view.tvText.text = message.text
                view.ivPhoto.visibility = View.GONE
            } else {
                view.tvText.visibility = View.GONE
                view.ivPhoto.visibility = View.VISIBLE
                Glide.with(view.ivPhoto.context).load(message.imageUrl).into(view.ivPhoto)
            }
        }
        return view
    }
}