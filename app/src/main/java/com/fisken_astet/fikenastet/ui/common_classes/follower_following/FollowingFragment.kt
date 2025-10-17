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
import com.fisken_astet.fikenastet.databinding.FragmentFollowingBinding
import com.fisken_astet.fikenastet.databinding.ItemFollowingBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : BaseFragment<FragmentFollowingBinding>() {
    private val viewModel: FollowerFollowingVM by viewModels()
    private lateinit var followingAdapter: SimpleRecyclerViewAdapter<FollowersModelData, ItemFollowingBinding>
    private var followingList = ArrayList<FollowersModelData>()
    private var listPosition: Int? = null
    private var myId:Int?=null
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
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
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
                        "FOLLOWING" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<FollowersModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        if (myDataModel.data != null) {
                                            val myId = sharedPrefManager.getLoginData()?.id
                                            followingList = (myDataModel.data as? ArrayList<FollowersModelData>)?.map { item ->
                                                item.apply { this.myId = myId }
                                            } as ArrayList<FollowersModelData>

                                            followingAdapter.list = followingList
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
                                            if (followingAdapter.list[listPosition!!].you_followed == false) {
                                                followingAdapter.list[listPosition!!].you_followed =
                                                    true
                                                followingAdapter.notifyItemChanged(
                                                    listPosition!!, null
                                                )
                                            } else {
                                                followingAdapter.list[listPosition!!].you_followed =
                                                    false
                                                followingAdapter.notifyItemChanged(
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
        followingAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_following, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.clMain -> {
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
        followingAdapter.list = followingList
        binding.rvFollower.adapter = followingAdapter

    }

    private fun apiCall(id: String) {
        val request = HashMap<String, Any>()
        request["type"] = "2"
        request["user_id"] = id
        viewModel.updateFollowActionApi(Constants.FOLLOW_ACTION, request, 2)
    }

    private fun apiCallToggle(id: String) {
        val request = HashMap<String, Any>()
        request["type"] = "3"
        request["user_id"] = id
        viewModel.updateFollowActionApi(Constants.FOLLOW_ACTION, request, 3)
    }

    private fun handleUi() {
        if (!followingAdapter.list.isNullOrEmpty()) {
            binding.rvFollower.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
            followingAdapter.notifyDataSetChanged()
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