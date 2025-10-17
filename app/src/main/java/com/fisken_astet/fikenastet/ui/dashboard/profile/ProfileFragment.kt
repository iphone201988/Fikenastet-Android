package com.fisken_astet.fikenastet.ui.dashboard.profile

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.GetProfileModel
import com.fisken_astet.fikenastet.data.model.GetProfileModelData
import com.fisken_astet.fikenastet.databinding.FragmentProfileBinding
import com.fisken_astet.fikenastet.ui.dashboard.DashBoardActivity
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.ui.dashboard.profile.profile_view_pager.CatchFeedFragment
import com.fisken_astet.fikenastet.ui.dashboard.profile.profile_view_pager.FeedFragment
import com.fisken_astet.fikenastet.ui.dashboard.profile.profile_view_pager.ForumFragment
import com.fisken_astet.fikenastet.ui.settings.SettingsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileFragmentVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_profile
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        binding.viewPager.isSaveEnabled = false
        // view
        initView()
        // click
        initOnClick()
        // observer
        initObserver()
    }

    /** handle view **/
    private fun initView(){
//        getProfile()
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        val icons = listOf(
            R.drawable.iv_grid,
            R.drawable.iv_add,
            R.drawable.iv_profile
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(icons[position])
        }.attach()

        // Listen for tab clicks
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    1 -> { // middle "Add" tab
                        val intent = Intent(requireActivity(), CommonActivity::class.java)
                        intent.putExtra("fromWhere", "AddFeed")
                        startActivity(intent)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                if (tab.position == 1) {
                    // Handle reselection too
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "AddFeed")
                    startActivity(intent)
                }
            }
        })
    }

    /** handle click **/
    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "CatchLog")
                    startActivity(intent)
                }
                R.id.ivNotification->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }

                R.id.ivSettings -> {
                    val activity = requireActivity() as? DashBoardActivity
                    if (activity?.isExtraFragmentVisible == true) {
                        return@observe
                    }
                    activity?.openExtraFragment(SettingsFragment())
                }

                R.id.tvPost -> {

                }

                R.id.tvEdit -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "EditProfile")
                    launcher.launch(intent)
                }

                R.id.tvShare -> {
                    val url ="https://dbt.teb.mybluehostin.me/share/profile/${sharedPrefManager.getLoginData()?.id}"
                    val shareText =  """Fiskenästet lets you capture your fishing moments and instantly share your catch status with others."""
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE,"FISKENASTET")
                        putExtra(Intent.EXTRA_SUBJECT,shareText)
                        putExtra(Intent.EXTRA_TEXT, url)
                    }

                    startActivity(Intent.createChooser(shareIntent, "FISKENASTET"))
                }

                R.id.tvFollowers,R.id.tvFollowersLabel -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Followers")
                    intent.putExtra("userId",sharedPrefManager.getLoginData()?.id.toString())
                    startActivity(intent)
                }

                R.id.tvFollowing,R.id.tvFollowingLabel -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Following")
                    intent.putExtra("userId",sharedPrefManager.getLoginData()?.id.toString())
                    startActivity(intent)
                }
            }
        }
    }

    /** handle observer **/
    private fun initObserver(){
        viewModel.profileObserver.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
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

    inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CatchFeedFragment()
                1 -> FeedFragment()
                2 -> ForumFragment()
                else -> CatchFeedFragment()
            }
        }
    }


    /** launch activity**/
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val isProfileUpdated =
                    result.data?.getBooleanExtra("isProfileUpdated", false) ?: false
                if (isProfileUpdated) {
                    getProfile()
                }
            }
        }

    private fun getProfile(){
        viewModel.getProfileData(Constants.PROFILE)
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as? DashBoardActivity
        if (activity?.isExtraFragmentVisible == true) {
            return
        }
        getProfile()
    }
}