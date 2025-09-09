package com.example.fikenastet.ui.dashboard.profile.threads

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.data.model.ThreadItemData
import com.example.fikenastet.data.model.ThreadListItem
import com.example.fikenastet.databinding.FragmentAllThreadsBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllThreadsFragment : BaseFragment<FragmentAllThreadsBinding>(), AllThreadsAdapter.ClickListeners {
    private val viewModel: ThreadsVM by viewModels()
    private lateinit var adapter: AllThreadsAdapter
    private var finalList: List<ThreadListItem> = emptyList()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_all_threads
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
        handleSearch()
    }

    private fun initView() {
        finalList = buildThreadList(threads)
        adapter = AllThreadsAdapter(finalList,requireContext(),this)
        binding.rvAllThreads.adapter = adapter

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{

                }
                R.id.consButton,R.id.textButton->{
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "NewThread")
                    startActivity(intent)
                }
            }
        }

    }

    private fun initObserver() {

    }

    private fun buildThreadList(rawThreads: List<ThreadItemData>): List<ThreadListItem> {
        val grouped = rawThreads.groupBy { it.header }

        val finalList = mutableListOf<ThreadListItem>()
        grouped.forEach { (header, entries) ->
            finalList.add(ThreadListItem.Header(header))
            finalList.addAll(
                entries.map {
                    ThreadListItem.ThreadData(ThreadItemData(it.header,it.title, it.author, it.replies, it.timeAgo,it.postedByMe))
                }
            )
        }
        return finalList
    }

    val threads = listOf(
        ThreadItemData("Trending", "Best Baits for Spring Pike?", "anglerTobias", 12, "2h ago"),
        ThreadItemData("Trending", "Fishing Rod Setup", "anglerMike", 5, "1h ago"),
        ThreadItemData("Latest Threads – This week", "Lake Fishing Tips", "anglerAnna", 8, "3h ago"),
        ThreadItemData("Latest Threads – This week", "Fly Fishing 101", "anglerSam", 20, "5h ago"),
        ThreadItemData("Your Threads", "Fly Fishing 101", "you", 20, "5h ago",true),
        ThreadItemData("Your Threads", "Fly Fishing 102", "you", 20, "5h ago",true),
        ThreadItemData("Catch of the week", "Fly Fishing 101", "you", 20, "5h ago")
    )

    override fun openDetail(pos: Int) {
        val intent = Intent(requireContext(), CommonActivity::class.java)
        intent.putExtra("fromWhere", "ThreadDetail")
        startActivity(intent)
    }

    override fun edit(pos: Int) {

    }

    override fun delete(pos: Int) {

    }

    /** handle search **/
    private fun handleSearch() {
        binding.textGetStarted.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()

                val filteredThreads = if (query.isEmpty()) {
                    threads
                } else {
                    threads.filter {
                        it.title.contains(query, ignoreCase = true) ||
                                it.author.contains(query, ignoreCase = true)
                    }
                }

                // rebuild grouped list with headers
                finalList = buildThreadList(filteredThreads)
                adapter.updateList(finalList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}