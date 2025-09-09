package com.example.fikenastet.ui.chat

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.DummyComment
import com.example.fikenastet.databinding.FragmentMessageListBinding
import com.example.fikenastet.databinding.ItemMessageBinding
import com.example.fikenastet.ui.selling.message.MessageVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageListFragment : BaseFragment<FragmentMessageListBinding>() {
    private val viewModel : ChatActivityVM by viewModels()
    private lateinit var adapterComment: SimpleRecyclerViewAdapter<DummyComment, ItemMessageBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_message_list
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView(view: View) {
       initView()
        initOnClick()
        initObserver()
    }

    private fun initView(){
        initAdapterComment()
        handleSearch()
    }

    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{

                }
            }
        }
    }

    private fun initObserver(){

    }

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
        DummyComment("Smith John "),
        DummyComment("John Doe "),
        DummyComment("Smith Williams ")

    )

    /** handle search **/
    private fun handleSearch() {
        binding.textGetStarted.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()

                val filteredList = if (query.isEmpty()) {
                    commentList
                } else {
                    commentList.filter {
                        it.message?.contains(query, ignoreCase = true) == true
                    }
                }

                adapterComment.list = filteredList.toMutableList()
                adapterComment.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}