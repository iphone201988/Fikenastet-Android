package com.fisken_astet.fikenastet.ui.selling

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fisken_astet.fikenastet.ui.selling.inbox.InboxFragment
import com.fisken_astet.fikenastet.ui.selling.message.MessageFragment
import com.fisken_astet.fikenastet.ui.selling.statistics.StatisticsFragment
import com.fisken_astet.fikenastet.ui.selling.your_listings.YourListingFragment

class ViewPagerSellingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InboxFragment()
            1 -> YourListingFragment()
            2 -> MessageFragment()
            3 -> StatisticsFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
