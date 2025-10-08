package com.fisken_astet.fikenastet.ui.settings

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.utils.BaseCustomDialog
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ExportDataModel
import com.fisken_astet.fikenastet.data.model.SimpleModel
import com.fisken_astet.fikenastet.databinding.CommonDialogDeleteLogoutBinding
import com.fisken_astet.fikenastet.databinding.FragmentSettingsBinding
import com.fisken_astet.fikenastet.databinding.ItemSettingsBinding
import com.fisken_astet.fikenastet.ui.auth.WelcomeActivity
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var settingsAdapter: SimpleRecyclerViewAdapter<String, ItemSettingsBinding>
    private lateinit var commonDialog: BaseCustomDialog<CommonDialogDeleteLogoutBinding>
    private var from :Int?=null
    override fun getLayoutResource(): Int {
        return R.layout.fragment_settings
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
        initDialog()
    }

    private fun initObserver() {
        viewModel.settingsObserver.observe(viewLifecycleOwner){
            when(it?.status){
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when(it.message){
                        "LOGOUT"->{
                            try {
                                val myDataModel = BindingUtils.parseJson<SimpleModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        sharedPrefManager.clear()
                                        showToast(myDataModel.message.toString())
                                        startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                                        requireActivity().finish()
                                    }
                                }


                            }
                            catch (e:Exception){
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                        "EXPORT_DATA"->{
                            try {
                                val myDataModel = BindingUtils.parseJson<ExportDataModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        showToast(myDataModel.message.toString())
                                        // download or share
                                    }
                                }
                            }
                            catch (e: Exception){
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    hideLoading()
                    try {
                        showToast(it.message.toString())
                    }
                    catch (e:Exception){
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }
                else -> {

                }
            }
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    parentFragmentManager.popBackStack()
                }

                R.id.ivNotification -> {

                }
                R.id.tvExport->{
//                    viewModel.exportDataApi(Constants.EXPORT_DATA)
                }

                R.id.tvLakePanel -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "MyLakes")
                    startActivity(intent)
                }
            }
        }

    }

    private fun initAdapter() {
        val settingsList = listOf(
            "Change Password",
            "Two-factor authentication",
            "Notifications",
            "Payment & Orders",
            "Blocked users",
            "Account integrity",
            "Invite friends",
            "Terms of Service",
            "Device permissions",
            "Log Out",
            "Delete Account"
        )
        settingsAdapter = SimpleRecyclerViewAdapter(R.layout.item_settings, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.tvSettings -> {
                    when (m) {
                        "Change Password" -> {
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "ChangePassword")
                            startActivity(intent)
                        }
                        "Two-factor authentication" -> {
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "2FA")
                            startActivity(intent)
                        }

                        "Notifications" -> {
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "NotificationsEnabled")
                            startActivity(intent)
                        }

                        "Payment & Orders" -> {
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "Payment")
                            startActivity(intent)
                        }
                        "Blocked users"->{
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "BlockedUser")
                            startActivity(intent)
                        }

                        "Account integrity"->{
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "AccountIntegrity")
                            startActivity(intent)
                        }
                        "Invite friends"->{
                            val url ="https://dbt.teb.mybluehostin.me/share/profile/${sharedPrefManager.getLoginData()?.id}"
                            val shareText =  """Fiskenästet lets you capture your fishing moments and instantly share your catch status with others."""
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TITLE,"FISKENASTET")
                                putExtra(Intent.EXTRA_SUBJECT,shareText)
                                putExtra(Intent.EXTRA_TEXT, url)
                            }

                            startActivity(Intent.createChooser(shareIntent, "FISKENASTET"))

                        }
                        "Terms of Service"->{

                        }

                        "Device permissions"->{
                            val intent = Intent(requireActivity(), CommonActivity::class.java)
                            intent.putExtra("fromWhere", "DevicePermissions")
                            startActivity(intent)
                        }

                        "Log Out"->{
                            from=1
                            handleDialogUI()
                            commonDialog.show()
                        }
                        "Delete Account"->{
                            from=2
                            handleDialogUI()
                            commonDialog.show()
                        }

                    }
                }
            }
        }
        settingsAdapter.list = settingsList
        binding.rvSettings.adapter = settingsAdapter

    }

    private fun initDialog(){
        commonDialog = BaseCustomDialog(requireActivity(),R.layout.common_dialog_delete_logout){
            when(it?.id){
                R.id.consButton2->{
                    if (from==1){
                        val request = HashMap<String, Any>()
                        viewModel.logoutApi(Constants.LOGOUT,request)
                    }
                    else{
                        // delete api
                    }

                }
                R.id.consButton->{
                    commonDialog.dismiss()
                }
            }
        }
    }

    private fun handleDialogUI(){
        if (from==1){
            commonDialog.binding.tvTitle.text="Logout Account"
            commonDialog.binding.tvSub.text="Are you sure to logout?"
            commonDialog.binding.textGetStarted2.text="Logout"
        }
        else{
            commonDialog.binding.tvTitle.text="Delete Account"
            commonDialog.binding.tvSub.text="Are you sure to delete the account?"
            commonDialog.binding.textGetStarted2.text="Delete Account"
        }
    }
}