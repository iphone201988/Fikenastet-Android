package com.example.fikenastet.ui.otherUserProfile

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentOtherUserBinding
import com.example.fikenastet.ui.chat.ChatActivity
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.example.fikenastet.ui.dashboard.profile.profile_view_pager.CatchFeedFragment
import com.example.fikenastet.ui.dashboard.profile.profile_view_pager.FeedFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherUserFragment : BaseFragment<FragmentOtherUserBinding>() {
    private val viewModel: OtherUserProfileVM by viewModels()
    private var isClicked = false
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
                    ///// report page is in profile-> threads
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "ReportAbuse")
                    startActivity(intent)
                }

                R.id.tvFollow -> {
                    isClicked = !isClicked
                    binding.tvFollow.text = if (isClicked) "Following" else "Follow"

                }

                R.id.tvMessage -> {
                    startActivity(Intent(requireActivity(), ChatActivity::class.java))
                }

                R.id.tvReviews -> {

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

    private fun initObserver() {

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

}