package com.fisken_astet.fikenastet.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fisken_astet.fikenastet.data.model.NotificationsItem
import com.fisken_astet.fikenastet.databinding.ItemDateViewBinding
import com.fisken_astet.fikenastet.databinding.ItemNotificationViewBinding

class NotificationAdapter(
    val context: Context,
    var list: MutableList<NotificationsItem>, // Add the listener here
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_DATE_HEADER = 0
        private const val VIEW_TYPE_NOTIFICATION = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is NotificationsItem.NotificationDate -> VIEW_TYPE_DATE_HEADER
            is NotificationsItem.NotificationData -> VIEW_TYPE_NOTIFICATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATE_HEADER -> {
                NotificationDateViewHolder(
                    ItemDateViewBinding.inflate(
                        LayoutInflater.from(
                            context
                        ), parent, false
                    )
                )
            }

            VIEW_TYPE_NOTIFICATION -> {
                NotificationViewHolder(
                    ItemNotificationViewBinding.inflate(
                        LayoutInflater.from(
                            context
                        ), parent, false
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is NotificationsItem.NotificationDate -> (holder as NotificationDateViewHolder).binding.apply {
                tvDate.text = item.date
            }

            is NotificationsItem.NotificationData -> (holder as NotificationViewHolder).binding.apply {
                tvName.text = item.notification.name
                tvTitle.text = item.notification.title

            }
        }
    }

    class NotificationDateViewHolder(itemView: ItemDateViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    class NotificationViewHolder(itemView: ItemNotificationViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }
}