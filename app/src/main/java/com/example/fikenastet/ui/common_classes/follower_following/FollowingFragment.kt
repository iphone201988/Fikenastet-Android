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
import com.example.fikenastet.databinding.FragmentFollowingBinding
import com.example.fikenastet.databinding.ItemFollowingBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : BaseFragment<FragmentFollowingBinding>() {
    private val viewModel: FollowerFollowingVM by viewModels()
    private lateinit var followingAdapter: SimpleRecyclerViewAdapter<FollowersFragmentModel, ItemFollowingBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_following
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
        followingAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_following, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.clMain -> {
                        val intent = Intent(requireActivity(), CommonActivity::class.java)
                        intent.putExtra("fromWhere", "OtherProfile")
                        startActivity(intent)
                    }
                }
            }
        followingAdapter.list = getList()
        binding.rvFollower.adapter = followingAdapter

    }

    private fun getList(): ArrayList<FollowersFragmentModel> {
        val list = ArrayList<FollowersFragmentModel>()
        list.add(FollowersFragmentModel(true))
        list.add(FollowersFragmentModel(false))
        list.add(FollowersFragmentModel(false))
        return list
    }

}