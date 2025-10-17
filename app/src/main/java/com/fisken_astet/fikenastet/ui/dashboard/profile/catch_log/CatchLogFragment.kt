package com.fisken_astet.fikenastet.ui.dashboard.profile.catch_log

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentCatchLogBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatchLogFragment : BaseFragment<FragmentCatchLogBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_catch_log
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
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        val tabText = listOf("Your Logged Catches", "Public Catches")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(tabText[position])
        }.attach()
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

    }

    inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> YourCatchesFragment()
                1 -> PublicCatchesFragment()
                else -> YourCatchesFragment()
            }
        }
    }

//    /** handle search **/
//    private fun handleSearch() {
//        binding.textGetStarted.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val query = s.toString().trim()
//
//                val filteredThreads = if (query.isEmpty()) {
//                    threads
//                } else {
//                    threads.filter {
//                        it.title.contains(query, ignoreCase = true) ||
//                                it.author.contains(query, ignoreCase = true)
//                    }
//                }
//
//                // rebuild grouped list with headers
//                finalList = buildThreadList(filteredThreads)
//                adapter.updateList(finalList)
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }

}