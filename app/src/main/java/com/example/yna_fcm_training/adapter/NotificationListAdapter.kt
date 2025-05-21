package com.example.yna_fcm_training.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yna_fcm_training.R

class NotificationListAdapter : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    private val notifications = mutableListOf<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNotification: TextView = view.findViewById(R.id.tvNotification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNotification.text = notifications[position]
    }

    override fun getItemCount() = notifications.size

    fun addNotification(notification: String) {
        notifications.add(0, notification)
        notifyItemInserted(0)
    }
}