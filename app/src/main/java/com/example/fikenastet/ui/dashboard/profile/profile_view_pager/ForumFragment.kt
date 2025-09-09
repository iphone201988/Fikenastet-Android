package com.example.fikenastet.ui.dashboard.profile.profile_view_pager

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.data.model.ThreadItemData
import com.example.fikenastet.data.model.ThreadListItem
import com.example.fikenastet.databinding.FragmentForumBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.example.fikenastet.ui.dashboard.profile.threads.AllThreadsAdapter
import com.example.fikenastet.ui.dashboard.profile.threads.ThreadsVM
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.component1
import kotlin.collections.component2

@AndroidEntryPoint
class ForumFragment : BaseFragment<FragmentForumBinding>(), AllThreadsAdapter.ClickListeners {
    private val viewModel: ThreadsVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_forum
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    private fun initView() {
        val finalList = buildThreadList(threads)
        val adapter = AllThreadsAdapter(finalList,requireContext(),this)
        binding.rvAllThreads.adapter = adapter
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.tvSeeAll->{
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "AllThreads")
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
}