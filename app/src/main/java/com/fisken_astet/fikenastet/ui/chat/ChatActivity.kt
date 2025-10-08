package com.fisken_astet.fikenastet.ui.chat

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseActivity
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>() {
    private val viewModel: ChatActivityVM by viewModels()
    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<ChatMessage>()
    override fun getLayoutResource(): Int {
        return R.layout.activity_chat
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initOnClick()
        initView()
    }

    private fun initView() {
        BindingUtils.statusBarStyleBlack(this)
        BindingUtils.styleSystemBars(this, getColor(R.color.black))
        chatAdapter = ChatAdapter(chatMessages)
        binding.rvChat.layoutManager = LinearLayoutManager(this)
        binding.rvChat.adapter = chatAdapter
        loadDummyMessages()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, Observer {
            when (it?.id) {
                R.id.ivBack -> {
                    finish()
                }
            }
        })

    }

    private fun loadDummyMessages() {
        chatMessages.add(ChatMessage(message = "Hi, how can I help you?", isUser = false))
        chatMessages.add(ChatMessage(message = "I want to buy something", isUser = true))
        chatMessages.add(
            ChatMessage(
                isProductCard = true,
                isUser = false, // optional but safe
                productTitle = "Wireless Headphones",
                productPrice = "$59.99",
                productImage = R.drawable.reel
            )
        )
        chatMessages.add(ChatMessage(message = "Thanks! Looks great.", isUser = true))

        chatAdapter.notifyDataSetChanged()
    }


}