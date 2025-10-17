package com.fisken_astet.fikenastet.ui.otherUserProfile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
    private var isRestricted: Boolean? = false
    private var isBlocked: Boolean? = false
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
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }

                R.id.ivDots -> {
                    handleBottomSheetUI()
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
                                                isRestricted = modelData.you_restricted
                                                isBlocked = modelData.you_blocked
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
                        "RESTRICT" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<GetProfileModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        showToast(myDataModel.message.toString())
                                        isRestricted = true
                                        handleBottomSheetUI()
                                    }
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "UNRESTRICT" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<GetProfileModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        showToast(myDataModel.message.toString())
                                        isRestricted = false
                                        handleBottomSheetUI()
                                    }
                                }
                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "BLOCK" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<GetProfileModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        showToast(myDataModel.message.toString())
                                        isBlocked = true
                                        handleBottomSheetUI()
                                    }
                                }
                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }

                        "UNBLOCK" -> {
                            try {
                                val myDataModel =
                                    BindingUtils.parseJson<GetProfileModel>(it.data.toString())
                                if (myDataModel != null) {
                                    if (myDataModel.status == 200) {
                                        showToast(myDataModel.message.toString())
                                        isBlocked = false
                                        handleBottomSheetUI()
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
                R.id.tvRestrict -> {
                    if (isRestricted == true) {
                        apiRestrictBlockToggle(4)
                    } else {
                        apiRestrictBlockToggle(3)
                    }
                }
                R.id.tvBlock->{
                    if (isBlocked == true) {
                        apiRestrictBlockToggle(2)
                    } else {
                        apiRestrictBlockToggle(1)
                    }

                }
                R.id.tvReport->{
                    profileOptionsBottomSheet.dismiss()
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "ReportAbuse")
                    intent.putExtra("id", userId)
                    intent.putExtra("from", "1")
                    startActivity(intent)
                }

                R.id.tvSharedActivity -> {

                }

                R.id.tvCopy -> {
                    val url = "https://dbt.teb.mybluehostin.me/share/profile/$userId"
                    val shareText = """
    FISKENASTET
    Fiskenästet lets you capture your fishing moments and instantly share your catch status with others. $url
""".trimIndent()

                    val clipboard =
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("FISKENASTET", shareText)
                    clipboard.setPrimaryClip(clip)
                    showToast("Url Copied")
                    profileOptionsBottomSheet.dismiss()
                }

                R.id.tvShare -> {
                    profileOptionsBottomSheet.dismiss()
                    val url = "https://dbt.teb.mybluehostin.me/share/profile/$userId"
                    val shareText =
                        """Fiskenästet lets you capture your fishing moments and instantly share your catch status with others."""
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE, "FISKENASTET")
                        putExtra(Intent.EXTRA_SUBJECT, shareText)
                        putExtra(Intent.EXTRA_TEXT, url)
                    }

                    startActivity(Intent.createChooser(shareIntent, "FISKENASTET"))
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

    private fun apiRestrictBlockToggle(type: Int) {
        val request = HashMap<String, Any>()
        request["type"] = type
        request["user_id"] = userId.toString()
        viewModel.handleRestrictBlockToggle(Constants.RESTRICT_BLOCK, request, type)
    }

    /** handle bottom sheet toggle**/
    private fun handleBottomSheetUI() {
        if (isRestricted == true) {
            profileOptionsBottomSheet.binding.tvRestrict.text = "Unrestrict"
            profileOptionsBottomSheet.binding.tvRestrict.setTextColor(requireContext().getColor(R.color.colorPrimary))
            profileOptionsBottomSheet.binding.tvRestrict.setBackgroundDrawable(
                requireContext().getDrawable(
                    R.drawable.radius_stroke_high
                )
            )
        } else {
            profileOptionsBottomSheet.binding.tvRestrict.text = "Restrict"
            profileOptionsBottomSheet.binding.tvRestrict.setTextColor(requireContext().getColor(R.color.red))
            profileOptionsBottomSheet.binding.tvRestrict.setBackgroundDrawable(
                requireContext().getDrawable(
                    R.drawable.bg_radius_stroke_red
                )
            )
        }

        if (isBlocked == true) {
            profileOptionsBottomSheet.binding.tvBlock.text = "Unblock"
            profileOptionsBottomSheet.binding.tvBlock.setTextColor(requireContext().getColor(R.color.colorPrimary))
            profileOptionsBottomSheet.binding.tvBlock.setBackgroundDrawable(
                requireContext().getDrawable(
                    R.drawable.radius_stroke_high
                )
            )
        } else {
            profileOptionsBottomSheet.binding.tvBlock.text = "Block"
            profileOptionsBottomSheet.binding.tvBlock.setTextColor(requireContext().getColor(R.color.red))
            profileOptionsBottomSheet.binding.tvBlock.setBackgroundDrawable(
                requireContext().getDrawable(
                    R.drawable.bg_radius_stroke_red
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        userId = arguments?.getString("id")
        if (userId != null) {
            getProfileData(userId.toString())
        }
    }



}