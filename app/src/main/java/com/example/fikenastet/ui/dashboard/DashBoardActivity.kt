package com.example.fikenastet.ui.dashboard

import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseActivity
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.utils.BindingUtils
import com.example.fikenastet.databinding.ActivityDashBaordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : BaseActivity<ActivityDashBaordBinding>() {
    private val viewModel: DashBoardActivityVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.activity_dash_baord
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initOnClick() {

    }

    private fun initView() {
        BindingUtils.statusBarStyleBlack(this)
        BindingUtils.styleSystemBars(this, getColor(R.color.black))
        setupNavigation()
        val data = intent.getStringExtra("from")
        if (data == "purchased") {
            binding.bottomNavigation.selectedItemId = R.id.nav_permit

        }
    }


    private fun setupNavigation() {
        binding.apply {

            val adapter = ViewPagerAdapter(this@DashBoardActivity)
            viewPager.adapter = adapter

            // Optional: Disable swipe if needed
            viewPager.isUserInputEnabled = false

            bottomNavigation.setOnItemSelectedListener { item ->
                clearExtraFragments()
                when (item.itemId) {
                    R.id.nav_home -> {
                        binding.viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(0, false)
                        true
                    }

                    R.id.nav_permit -> {
                        binding.viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(1, false)
                        true
                    }

                    R.id.nav_map -> {
                        binding.viewLine.setBackgroundColor(getColor(R.color.app_text_hint))
                        viewPager.setCurrentItem(2, false)
                        true
                    }

                    R.id.nav_market -> {
                        binding.viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(3, false)
                        true
                    }

                    R.id.nav_profile -> {
                        binding.viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(4, false)
                        true
                    }

                    else -> false
                }
            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> bottomNavigation.selectedItemId = R.id.nav_home
                        1 -> bottomNavigation.selectedItemId = R.id.nav_permit
                        2 -> bottomNavigation.selectedItemId = R.id.nav_map
                        3 -> bottomNavigation.selectedItemId = R.id.nav_market
                        4 -> bottomNavigation.selectedItemId = R.id.nav_profile
                    }
                }
            })
        }
    }

    fun openExtraFragment(fragment: Fragment) {
        binding.extraFragment.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.extraFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun clearExtraFragments() {
        // Clear back stack only for extra container
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        binding.extraFragment.visibility = View.GONE
    }

    override fun onBackPressed() {
        val extraFragment = supportFragmentManager.findFragmentById(R.id.extraFragment)

        if (extraFragment != null) {
            // If an overlay fragment is active, pop it
            supportFragmentManager.popBackStack()
            if (supportFragmentManager.findFragmentById(R.id.extraFragment) == null) {
                binding.extraFragment.visibility = View.GONE
            }
        } else {
            // No overlay fragment → normal back behavior
            super.onBackPressed()
        }
    }


}