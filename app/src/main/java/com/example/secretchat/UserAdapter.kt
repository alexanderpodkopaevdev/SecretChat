package com.example.secretchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var listener: OnUserClickListener

    interface OnUserClickListener {
        fun onUserClick(position: Int)
    }

    fun setOnUserClickListener(listener: OnUserClickListener) {
        this.listener = listener
    }

    class UserViewHolder(itemView: View, listener: OnUserClickListener?) :
        RecyclerView.ViewHolder(itemView) {


        val avatar = itemView.ivItemUserAvatar
        val userName = itemView.tvItemUserName

        init {
            itemView.setOnClickListener {
                if (listener != null) {
                    if (adapterPosition != RecyclerView.NO_POSITION)
                        listener.onUserClick(adapterPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_item,
                parent,
                false
            ), listener)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userItem = users[position]
        holder.userName.text = userItem.name
        holder.avatar.setImageResource(userItem.avatar ?: R.drawable.user_image)
    }
}