package com.fisken_astet.fikenastet.ui.otherUserProfile

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BaseCustomBottomSheet
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.FollowUnfollowToggleModel
import com.fisken_astet.fikenastet.data.model.GetProfileModel
import com.fisken_astet.fikenastet.databinding.FragmentOtherUserBinding
import com.fisken_astet.fikenastet.databinding.ProfileOptionsBottomSheetBinding
import com.fisken_astet.fikenastet.ui.chat.ChatActivity
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.ui.dashboard.profile.profile_view_pager.CatchFeedFragment
import com.fisken_astet.fikenastet.ui.dashboard.profile.profile_view_pager.FeedFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherUserFragment : BaseFragment<FragmentOtherUserBinding>() {
    private val viewModel: OtherUserProfileVM by viewModels()
    private lateinit var profileOptionsBottomSheet: BaseCustomBottomSheet<ProfileOptionsBottomSheetBinding>
    private var userId:String?=null
    override fun getLayoutResource(): Int {
        return R.layout.fragment_other_user
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initObserver()
        initOnClick()
    }

    private fun initView() {
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        val icons = listOf(
            R.drawable.iv_grid,
            R.drawable.iv_group
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        initBottomSheet()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {

                }

                R.id.ivDots -> {
                    profileOptionsBottomSheet.show()
                }

                R.id.tvFollow -> {
                    apiCallToggle()

                }

                R.id.tvMessage -> {
                    startActivity(Intent(requireActivity(), ChatActivity::class.java))
                }

                R.id.tvReviews -> {

                }

                R.id.tvFollowers,R.id.tvFollowersLabel -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Followers")
                    intent.putExtra("userId",userId)
                    startActivity(intent)
                }

                R.id.tvFollowing,R.id.tvFollowingLabel -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Following")
                    intent.putExtra("userId",userId)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.otherProfileObserver.observe(viewLifecycleOwner){
            when(it?.status){
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when(it.message){
                        "PROFILE_DATA" -> {
                            try {
                                val myDataModel = BindingUtils.parseJson<GetProfileModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        if (myDataModel.data!=null){
                                            val modelData = myDataModel.data
                                            binding.bean=modelData
                                            if (!modelData.links.isNullOrEmpty()){
                                                binding.tvLink.setText(modelData.links[0]?.url)
                                            }
                                        }
                                    }
                                }
                            }
                            catch (e:Exception){
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
                                        if (myDataModel.action == "followed") {
                                            binding.tvFollow.text="Following"
                                        } else {
                                            binding.tvFollow.text="Follow"
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
                    }
                    catch (e:Exception){
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }
                else -> {

                }
            }
        }

    }

    inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CatchFeedFragment()
                1 -> FeedFragment()
                else -> CatchFeedFragment()
            }
        }
    }

    private fun initBottomSheet(){
        profileOptionsBottomSheet= BaseCustomBottomSheet(requireContext(),R.layout.profile_options_bottom_sheet,R.style.SheetDialog){
            when(it?.id){
                R.id.tvBlock->{

                }
                R.id.tvReport->{
                    profileOptionsBottomSheet.dismiss()
                                        val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "ReportAbuse")
                    startActivity(intent)
                }
                R.id.tvRemove->{

                }
            }
        }
    }

    /** api call **/
    private fun getProfileData(userId:String){
        viewModel.getProfileData("${Constants.PROFILE}/$userId")
    }


    private fun apiCallToggle() {
        val request = HashMap<String, Any>()
        request["type"] = "3"
        request["user_id"] = userId.toString()
        viewModel.updateFollowActionApi(Constants.FOLLOW_ACTION, request)
    }

    override fun onResume() {
        super.onResume()
        userId = arguments?.getString("id")
        if (userId!=null) {
            getProfileData(userId.toString())
        }
    }



}