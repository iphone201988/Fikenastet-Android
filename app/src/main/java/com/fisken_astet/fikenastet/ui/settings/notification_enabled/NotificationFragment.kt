package com.fisken_astet.fikenastet.ui.settings.notification_enabled

import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.NotificationToggleModel
import com.fisken_astet.fikenastet.databinding.FragmentNotificationBinding
import com.fisken_astet.fikenastet.databinding.HolderSkillsBinding
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var notificationAdapter: SimpleRecyclerViewAdapter<String, HolderSkillsBinding>
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
        val user = sharedPrefManager.getLoginData()
        if (user!=null && user.push_notifications!=null){
            when(user.push_notifications){
                0->binding.tvCategory.text="Disabled"
                else -> binding.tvCategory.text="Enabled"
            }
        }
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
                R.id.ivDropArrow,R.id.tvCategory -> {
                    isClicked = !isClicked
                    binding.rvNotifications.visibility = if (isClicked) View.VISIBLE else View.GONE
                    binding.ivDropArrow.rotation = if (isClicked) 180f else 0f
                }

                R.id.consButton->{
                    BindingUtils.preventMultipleClick(it)
                    notificationToggleApi()
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.settingsObserver.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showLoading()
                }

                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "NOTIFICATION_TOGGLE" -> {
                            try {
                                val myDataModel= BindingUtils.parseJson<NotificationToggleModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.success==200){
                                        showToast(myDataModel.message.toString())
                                        val user = sharedPrefManager.getLoginData()
                                        if (user!=null){
                                            user.push_notifications=myDataModel.push_notifications
                                            sharedPrefManager.setLoginData(user)
                                        }
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
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

    private fun initAdapter() {
        val categoryList = listOf(
            "Enabled","Disabled"
        )
        notificationAdapter =
            SimpleRecyclerViewAdapter(R.layout.holder_skills, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.tvLevel -> {
                        binding.tvCategory.text=m
                        binding.rvNotifications.visibility= View.GONE
                        binding.ivDropArrow.rotation = 0f
                    }
                }
            }
        notificationAdapter.list = categoryList
        binding.rvNotifications.adapter = notificationAdapter
    }

    /** api call **/
    private fun notificationToggleApi() {
        val toggleState = when (binding.tvCategory.text) {
            "Enabled" -> 1
            "Disabled" -> 0
            else -> 1
        }
        val request = HashMap<String, Any>()
        request["type"] = toggleState
        viewModel.notificationToggleApi(Constants.NOTIFICATION_SETTINGS, request)
    }


}