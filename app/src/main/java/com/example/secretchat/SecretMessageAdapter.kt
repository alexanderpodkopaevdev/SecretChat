package com.example.secretchat

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.message_item.view.*

class SecretMessageAdapter(context: Activity, resource: Int,
                           private val messages: List<SecretMessage>
) :
    ArrayAdapter<SecretMessage>(context, resource, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val secretMessage = getItem(position)
        val viewType = getItemViewType(position)
        val layoutResource = if (viewType == 0) R.layout.my_message_item else R.layout.your_message_item
        lateinit var viewHolder: ViewHolder
        lateinit var view : View
        if (convertView != null) {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        } else {
            view = inflater.inflate(layoutResource,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }

        if (secretMessage!= null) {

            if (secretMessage.imageUrl.isNullOrEmpty()) {
                viewHolder.textMessage.visibility = View.VISIBLE
                viewHolder.photoMessage.visibility = View.GONE
                viewHolder.textMessage.text = secretMessage.text
            } else {
                viewHolder.textMessage.visibility = View.GONE
                viewHolder.photoMessage.visibility = View.VISIBLE
                Glide.with(viewHolder.photoMessage.context).load(secretMessage.imageUrl).into(viewHolder.photoMessage)
            }
        }
        return view
    }

    override fun getItemViewType(position: Int) = if (messages[position].isMine) 0 else 1

    override fun getViewTypeCount() = 2

    class ViewHolder(view: View) {
        val photoMessage = view.findViewById(R.id.ivMessagePhoto) as ImageView
        val textMessage = view.findViewById(R.id.tvMessageText) as TextView
    }
}