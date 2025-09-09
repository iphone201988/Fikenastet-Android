package com.example.fikenastet.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.utils.BindingUtils.formatDate
import com.example.fikenastet.data.model.NotificationModelData
import com.example.fikenastet.data.model.NotificationsItem
import com.example.fikenastet.databinding.FragmentNotificationsBinding
import com.example.fikenastet.ui.settings.notification_enabled.NotificationFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {
    private val viewModel: NotificationVM by viewModels()
    private lateinit var notificationsAdapter: NotificationAdapter
    private var notificationList: MutableList<NotificationsItem> = ArrayList()
    override fun getLayoutResource(): Int {
       return R.layout.fragment_notifications
    }

    override fun getViewModel(): BaseViewModel {
      return viewModel
    }

    override fun onCreateView(view: View) {
       initView()
        initOnClick()
        initObserver()
    }

    private fun initView(){
        notificationList =
            groupNotificationsByDate(getNotificationsList()) as ArrayList
            notificationsAdapter = NotificationAdapter(requireActivity(), notificationList)
            binding.rvNotification.adapter = notificationsAdapter
    }

    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.consClear->{
                    notificationsAdapter.list.clear()
                    notificationsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initObserver(){

    }

    private fun groupNotificationsByDate(notifications: List<NotificationModelData?>?): List<NotificationsItem> {
        val groupedNotifications = linkedMapOf<String, MutableList<NotificationModelData>>()

        if (notifications != null) {
            for (notification in notifications) {
                val date = formatDate(notification?.createdAt!!)
                groupedNotifications.getOrPut(date.first) { mutableListOf() }.add(notification)
            }
        }

        val items = mutableListOf<NotificationsItem>()
        for ((date, notificationsForDate) in groupedNotifications) {
            if (!notificationList.contains(NotificationsItem.NotificationDate(date))) {
                items.add(NotificationsItem.NotificationDate(date))
            }
            notificationsForDate.forEach {
                items.add(NotificationsItem.NotificationData(it))
            }
        }

        return items
    }

    private fun getNotificationsList():ArrayList<NotificationModelData>{
        val list=ArrayList<NotificationModelData>()
        list.add(NotificationModelData("@Ella","Your item “Shimano Reel” was purchased!","2025-09-09T09:15:23.123Z"))
        list.add(NotificationModelData("@Peter","Record your symptoms for today","2025-09-008T09:15:23.123Z"))
        list.add(NotificationModelData("@John Doe","How are you feeling today ?","2025-07-29T18:30:05.456Z"))
        list.add(NotificationModelData("@Sam","Record your symptoms for today","2025-07-29T18:30:05.456Z"))
        list.add(NotificationModelData("@Lily","New notification","2025-07-29T18:30:05.456Z"))
        list.add(NotificationModelData("@Bella","New notification","2025-07-27T22:10:45.001Z"))
        return list
    }

}