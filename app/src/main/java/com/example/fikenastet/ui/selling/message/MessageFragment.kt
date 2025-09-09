package com.example.fikenastet.ui.selling.message

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.DummyComment
import com.example.fikenastet.databinding.FragmentMessageBinding
import com.example.fikenastet.databinding.ItemMessageBinding
import com.example.fikenastet.ui.chat.ChatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : BaseFragment<FragmentMessageBinding>() {
    private val viewModel: MessageVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_message
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
            }
        }
    }

    private fun initView() {
        initAdapterComment()
    }

    private lateinit var adapterComment: SimpleRecyclerViewAdapter<DummyComment, ItemMessageBinding>
    private fun initAdapterComment() {
        adapterComment =
            SimpleRecyclerViewAdapter(R.layout.item_message, BR.bean) { view, value, _ ->
                when(view.id){
                    R.id.clMain->{
                        startActivity(Intent(requireActivity(),ChatActivity::class.java))
                    }
                }
            }
        binding.rvComment.adapter = adapterComment
        adapterComment.list = commentList
    }

    val commentList = arrayListOf(
        DummyComment("Smith Williams "),
        DummyComment("Smith Williams "),
        DummyComment("Smith Williams "),
        DummyComment("Smith Williams ")

    )


}