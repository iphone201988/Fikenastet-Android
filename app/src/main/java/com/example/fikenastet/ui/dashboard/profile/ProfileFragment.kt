package com.example.fikenastet.ui.dashboard.profile

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentProfileBinding
import com.example.fikenastet.ui.dashboard.DashBoardActivity
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.example.fikenastet.ui.dashboard.profile.profile_view_pager.CatchFeedFragment
import com.example.fikenastet.ui.dashboard.profile.profile_view_pager.FeedFragment
import com.example.fikenastet.ui.dashboard.profile.profile_view_pager.ForumFragment
import com.example.fikenastet.ui.settings.SettingsFragment
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
    }

    /** handle click **/
    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivNotification->{

                }

                R.id.ivSettings -> {
                    (requireActivity() as DashBoardActivity).openExtraFragment(SettingsFragment())
                }

                R.id.tvPost -> {

                }

                R.id.tvEdit -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "EditProfile")
                    startActivity(intent)
                }

                R.id.tvShare -> {

                }

                R.id.tvFollowers -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Followers")
                    startActivity(intent)
                }

                R.id.tvFollowing -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Following")
                    startActivity(intent)
                }
            }
        }
    }

    /** handle observer **/
    private fun initObserver(){

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
}