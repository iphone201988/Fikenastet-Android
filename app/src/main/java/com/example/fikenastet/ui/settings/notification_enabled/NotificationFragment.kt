package com.example.fikenastet.ui.settings.notification_enabled

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentNotificationBinding
import com.example.fikenastet.databinding.ItemThreadsCategoryBinding
import com.example.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var notificationAdapter: SimpleRecyclerViewAdapter<String, ItemThreadsCategoryBinding>
    private var isClicked:Boolean=false
    override fun getLayoutResource(): Int {
        return R.layout.fragment_notification
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
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{

                }
                R.id.ivDropArrow -> {
                    isClicked = !isClicked
                    binding.rvNotifications.visibility = if (isClicked) View.VISIBLE else View.GONE
                    binding.ivDropArrow.rotation = if (isClicked) 180f else 0f
                }

                R.id.consButton->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        val categoryList = listOf(
            "Enabled","Disabled"
        )
        notificationAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_threads_category, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.clMain -> {
                        binding.tvCategory.text=m
                        binding.rvNotifications.visibility= View.GONE
                        binding.ivDropArrow.rotation = 0f
                    }
                }
            }
        notificationAdapter.list = categoryList
        binding.rvNotifications.adapter = notificationAdapter
    }


}