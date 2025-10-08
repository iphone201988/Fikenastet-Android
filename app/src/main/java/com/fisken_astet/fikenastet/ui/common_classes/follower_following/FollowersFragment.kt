package com.fisken_astet.fikenastet.ui.common_classes.follower_following

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.FollowUnfollowToggleModel
import com.fisken_astet.fikenastet.data.model.FollowersModel
import com.fisken_astet.fikenastet.data.model.FollowersModelData
import com.fisken_astet.fikenastet.databinding.FragmentFollowersBinding
import com.fisken_astet.fikenastet.databinding.ItemFollowerBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseFragment<FragmentFollowersBinding>() {
    private val viewModel: FollowerFollowingVM by viewModels()
    private lateinit var followerAdapter: SimpleRecyclerViewAdapter<FollowersModelData, ItemFollowerBinding>
    private var followersList = ArrayList<FollowersModelData>()
    private var listPosition: Int? = null
    private var myId:Int?=null
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
        myId=sharedPrefManager.getLoginData()?.id
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
        viewModel.followObserver.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "FOLLOWER" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<FollowersModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        if (myDataModel.data != null) {
                                            val myId = sharedPrefManager.getLoginData()?.id
                                            followersList = (myDataModel.data as? ArrayList<FollowersModelData>)?.map { item ->
                                                item.apply { this.myId = myId }
                                            } as ArrayList<FollowersModelData>

                                            followerAdapter.list = followersList
                                        }
                                    }
                                }
                                handleUi()
                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "FOLLOW_UNFOLLOW" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<FollowUnfollowToggleModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        showToast(myDataModel.message.toString())
                                        if (listPosition != null) {
                                            if (followerAdapter.list[listPosition!!].you_followed == false) {
                                                followerAdapter.list[listPosition!!].you_followed =
                                                    true
                                                followerAdapter.notifyItemChanged(
                                                    listPosition!!, null
                                                )
                                            } else {
                                                followerAdapter.list[listPosition!!].you_followed =
                                                    false
                                                followerAdapter.notifyItemChanged(
                                                    listPosition!!, null
                                                )
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    hideLoading()
                    try {
                        showToast(it.message.toString())
                    } catch (e: Exception) {
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }

                else -> {

                }
            }
        }

    }

    private fun initAdapter() {
        followerAdapter = SimpleRecyclerViewAdapter(R.layout.item_follower, BR.bean) { v, m, pos ->
            when (v.id) {
                R.id.clMain->{
                    if (myId!=null && myId==m.id) {
                        return@SimpleRecyclerViewAdapter
                    }
                        val intent = Intent(requireActivity(), CommonActivity::class.java)
                        intent.putExtra("fromWhere", "OtherProfile")
                        intent.putExtra("id", m.id.toString())
                        startActivity(intent)
                }
                R.id.tvFollowStatus -> {
                    if (myId!=null && myId==m.id) {
                        return@SimpleRecyclerViewAdapter
                    }
                    BindingUtils.preventMultipleClick(v)
                    listPosition = pos
                    apiCallToggle(m.id.toString())
                }
            }
        }
        followerAdapter.list = followersList
        binding.rvFollower.adapter = followerAdapter

    }

    /** api call **/
    private fun apiCall(id: String) {
        val request = HashMap<String, Any>()
        request["type"] = "1"
        request["user_id"] = id
        viewModel.updateFollowActionApi(Constants.FOLLOW_ACTION, request, 1)
    }

    private fun apiCallToggle(id: String) {
        val request = HashMap<String, Any>()
        request["type"] = "3"
        request["user_id"] = id
        viewModel.updateFollowActionApi(Constants.FOLLOW_ACTION, request, 3)
    }

    private fun handleUi() {
        if (!followerAdapter.list.isNullOrEmpty()) {
            binding.rvFollower.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
            followerAdapter.notifyDataSetChanged()
        } else {
            binding.rvFollower.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        val userId = arguments?.getString("userId")
        if (userId != null) {
            apiCall(userId)
        }
    }
}