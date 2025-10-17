package com.fisken_astet.fikenastet.ui.dashboard

import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseActivity
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.databinding.ActivityDashBaordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : BaseActivity<ActivityDashBaordBinding>() {

    private val viewModel: DashBoardActivityVM by viewModels()
    var isExtraFragmentVisible = false
        private set

    override fun getLayoutResource(): Int = R.layout.activity_dash_baord

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView() {
        initView()
        initOnClick()

        // ✅ Automatically sync the flag with fragment stack
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.extraFragment)
            isExtraFragmentVisible = currentFragment != null
            binding.extraFragment.visibility = if (currentFragment != null) View.VISIBLE else View.GONE
        }
    }

    private fun initOnClick() {
        // Set click listeners here if needed
    }

    private fun initView() {
        BindingUtils.statusBarStyleBlack(this)
        BindingUtils.styleSystemBars(this, getColor(R.color.black))
        setupNavigation()

        val data = intent.getStringExtra("from")
        when (data) {
            "purchased" -> binding.bottomNavigation.selectedItemId = R.id.nav_permit
            "map" -> binding.bottomNavigation.selectedItemId = R.id.nav_map
        }
    }

    private fun setupNavigation() {
        binding.apply {
            val adapter = ViewPagerAdapter(this@DashBoardActivity)
            viewPager.adapter = adapter
            viewPager.isUserInputEnabled = false // disable swipe if needed

            bottomNavigation.setOnItemSelectedListener { item ->
                clearExtraFragments()
                when (item.itemId) {
                    R.id.nav_home -> {
                        viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(0, false)
                        true
                    }

                    R.id.nav_permit -> {
                        viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(1, false)
                        true
                    }

                    R.id.nav_map -> {
                        viewLine.setBackgroundColor(getColor(R.color.app_text_hint))
                        viewPager.setCurrentItem(2, false)
                        true
                    }

                    R.id.nav_market -> {
                        viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(3, false)
                        true
                    }

                    R.id.nav_profile -> {
                        viewLine.setBackgroundColor(getColor(R.color.colorPrimary))
                        viewPager.setCurrentItem(4, false)
                        true
                    }

                    else -> false
                }
            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bottomNavigation.selectedItemId = when (position) {
                        0 -> R.id.nav_home
                        1 -> R.id.nav_permit
                        2 -> R.id.nav_map
                        3 -> R.id.nav_market
                        else -> R.id.nav_profile
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
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        binding.extraFragment.visibility = View.GONE
        isExtraFragmentVisible = false
    }

    override fun onResume() {
        super.onResume()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.extraFragment)
        isExtraFragmentVisible = currentFragment != null
        binding.extraFragment.visibility = if (currentFragment != null) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        val extraFragment = supportFragmentManager.findFragmentById(R.id.extraFragment)
        if (extraFragment != null) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}