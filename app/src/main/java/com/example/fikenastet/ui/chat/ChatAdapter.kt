package com.example.fikenastet.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fikenastet.R

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_RECEIVER = 2
    private val VIEW_TYPE_PRODUCT = 3
    override fun getItemViewType(position: Int): Int {
        val msg = messages[position]
        return when {
            msg.isProductCard -> VIEW_TYPE_PRODUCT
            msg.isUser -> VIEW_TYPE_USER
            else -> VIEW_TYPE_RECEIVER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = inflater.inflate(R.layout.item_user_message, parent, false)
                UserMessageViewHolder(view)
            }

            VIEW_TYPE_PRODUCT -> {
                val view = inflater.inflate(R.layout.item_product_card, parent, false)
                ProductViewHolder(view)
            }

            else -> {
                val view = inflater.inflate(R.layout.item_receiver_message, parent, false)
                ReceiverMessageViewHolder(view)
            }
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is ReceiverMessageViewHolder -> holder.bind(message)
            is ProductViewHolder -> holder.bind(message)
        }
    }

    class UserMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.tvMessage)
        fun bind(message: ChatMessage) {
            text.text = message.message
        }
    }

    class ReceiverMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.tvMessage)
        fun bind(message: ChatMessage) {
            text.text = message.message
        }
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.tvTitle)
        private val price = view.findViewById<TextView>(R.id.tvPrice)
        private val image = view.findViewById<ImageView>(R.id.ivProduct)

        fun bind(message: ChatMessage) {
            title.text = message.productTitle
            price.text = message.productPrice
            image.setImageResource(message.productImage ?: 0)
        }
    }
}
