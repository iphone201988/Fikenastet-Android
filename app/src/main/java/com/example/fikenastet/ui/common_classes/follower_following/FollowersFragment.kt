package com.example.fikenastet.ui.common_classes.follower_following

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.FollowersFragmentModel
import com.example.fikenastet.databinding.FragmentFollowersBinding
import com.example.fikenastet.databinding.ItemFollowerBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseFragment<FragmentFollowersBinding>() {
    private val viewModel: FollowerFollowingVM by viewModels()
    private lateinit var followerAdapter: SimpleRecyclerViewAdapter<FollowersFragmentModel, ItemFollowerBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_followers
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
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {

                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        followerAdapter = SimpleRecyclerViewAdapter(R.layout.item_follower, BR.bean) { v, m, pos ->
            when (v.id) {
                R.id.clMain->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "OtherProfile")
                    startActivity(intent)
                }
                R.id.tvFollowStatus -> {
                    if (followerAdapter.list[pos].selected == false) {
                        followerAdapter.list[pos].selected = true
                        followerAdapter.notifyItemChanged(pos, m)
                    } else {
                        followerAdapter.list[pos].selected = false
                        followerAdapter.notifyItemChanged(pos, m)
                    }
                }
            }
        }
        followerAdapter.list = getList()
        binding.rvFollower.adapter = followerAdapter

    }

    private fun getList(): ArrayList<FollowersFragmentModel> {
        val list = ArrayList<FollowersFragmentModel>()
        list.add(FollowersFragmentModel(true))
        list.add(FollowersFragmentModel(false))
        list.add(FollowersFragmentModel(false))
        return list
    }
}