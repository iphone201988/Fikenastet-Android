package com.example.fikenastet.ui.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fikenastet.ui.dashboard.home.HomeFragment
import com.example.fikenastet.ui.dashboard.map.MapFragment
import com.example.fikenastet.ui.dashboard.marketplace.MarketplaceFragment
import com.example.fikenastet.ui.dashboard.permits.PermitFragment
import com.example.fikenastet.ui.dashboard.profile.ProfileFragment
import com.example.fikenastet.ui.selling.inbox.InboxFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> PermitFragment()
            2 -> MapFragment()
            3 -> MarketplaceFragment()
            4 -> ProfileFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
