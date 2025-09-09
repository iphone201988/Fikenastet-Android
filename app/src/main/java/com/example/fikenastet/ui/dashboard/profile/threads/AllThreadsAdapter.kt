package com.example.fikenastet.ui.dashboard.profile.threads

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fikenastet.data.model.ThreadListItem
import com.example.fikenastet.databinding.ItemThreadHeaderBinding
import com.example.fikenastet.databinding.ItemThreadViewBinding

class AllThreadsAdapter(private var items: List<ThreadListItem>, val context: Context,val listener: ClickListeners) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_THREAD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ThreadListItem.Header -> TYPE_HEADER
            is ThreadListItem.ThreadData -> TYPE_THREAD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderViewHolder(
                    ItemThreadHeaderBinding.inflate(
                        LayoutInflater.from(
                            context
                        ), parent, false
                    )
                )
            }

            TYPE_THREAD -> {
                ThreadViewHolder(
                    ItemThreadViewBinding.inflate(
                        LayoutInflater.from(
                            context
                        ), parent, false
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ThreadListItem.Header -> (holder as HeaderViewHolder).binding.apply {
                tvHeader.text = item.title
            }

            is ThreadListItem.ThreadData -> (holder as ThreadViewHolder).binding.apply {
                tvTitle.text = item.data.title
                tvPostedBy.text = "by @${item.data.author}"
                tvReplies.text = "${item.data.replies} replies"
                tvPostedTime.text = item.data.timeAgo
                if (item.data.postedByMe){
                    ivEdit.visibility= View.VISIBLE
                    tvEdit.visibility= View.VISIBLE
                    ivDelete.visibility= View.VISIBLE
                    tvDelete.visibility= View.VISIBLE
                }
                else{
                    ivEdit.visibility= View.GONE
                    tvEdit.visibility= View.GONE
                    ivDelete.visibility= View.GONE
                    tvDelete.visibility= View.GONE
                }
                ivDropArrow.setOnClickListener {
                    listener.openDetail(position)
                }
            }
        }
    }

    override fun getItemCount() = items.size

    class HeaderViewHolder(itemView: ItemThreadHeaderBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    class ThreadViewHolder(itemView: ItemThreadViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    interface ClickListeners{
        fun openDetail(pos: Int)
        fun edit(pos:Int)
        fun delete(pos: Int)
    }

    fun updateList(newList: List<ThreadListItem>) {
        items = newList
        notifyDataSetChanged()
    }
}