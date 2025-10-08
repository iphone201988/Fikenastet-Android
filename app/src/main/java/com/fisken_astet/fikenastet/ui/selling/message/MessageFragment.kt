package com.fisken_astet.fikenastet.ui.selling.message

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.data.model.DummyComment
import com.fisken_astet.fikenastet.databinding.FragmentMessageBinding
import com.fisken_astet.fikenastet.databinding.ItemMessageBinding
import com.fisken_astet.fikenastet.ui.chat.ChatActivity
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