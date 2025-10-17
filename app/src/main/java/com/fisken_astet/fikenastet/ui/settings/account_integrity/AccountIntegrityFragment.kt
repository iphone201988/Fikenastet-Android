package com.fisken_astet.fikenastet.ui.settings.account_integrity

import android.content.Intent
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
import com.fisken_astet.fikenastet.data.model.AccountIntegrityModel
import com.fisken_astet.fikenastet.databinding.FragmentAccountIntegrityBinding
import com.fisken_astet.fikenastet.databinding.HolderSkillsBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountIntegrityFragment : BaseFragment<FragmentAccountIntegrityBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var integrityAdapter: SimpleRecyclerViewAdapter<String, HolderSkillsBinding>
    private var isClicked:Boolean=false
    override fun getLayoutResource(): Int {
        return R.layout.fragment_account_integrity
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        // handle view
        initView()
        // handle click
        initOnClick()
        // handle observer
        initObserver()
    }

    /** handle view **/
    private fun initView(){
        val user = sharedPrefManager.getLoginData()
        if (user!=null && user.push_notifications!=null){
            when(user.account_integrity){
                "private"->binding.tvAccountType.text="Private Account"
                else -> binding.tvAccountType.text="Public Account"
            }
        }
        initAdapter()
    }

    /** handle click **/
    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }
                R.id.ivDropArrow,R.id.tvAccountType -> {
                    isClicked = !isClicked
                    binding.rvAccountType.visibility = if (isClicked) View.VISIBLE else View.GONE
                    binding.ivDropArrow.rotation = if (isClicked) 180f else 0f
                }
                R.id.consButton->{
                    BindingUtils.preventMultipleClick(it)
                    accountToggle()
                }
            }
        }

    }

    /** handle observer**/
    private fun initObserver(){
        viewModel.settingsObserver.observe(viewLifecycleOwner){
            when(it?.status){
                Status.SUCCESS -> {
                    hideLoading()
                    when(it.message){
                        "ACCOUNT_TOGGLE"->{
                            try {
                                val myDataModel = BindingUtils.parseJson<AccountIntegrityModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        showToast(myDataModel.message.toString())
                                        val user = sharedPrefManager.getLoginData()
                                        if (user!=null){
                                            user.account_integrity=myDataModel.data?.account_integrity
                                            sharedPrefManager.setLoginData(user)
                                        }
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }
                                }
                            }
                            catch (e:Exception){
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
                Status.LOADING -> showLoading()
                else -> {}
            }
        }

    }

    // adapter
    private fun initAdapter() {
        val categoryList = listOf(
            "Private Account","Public Account"
        )
        integrityAdapter =
            SimpleRecyclerViewAdapter(R.layout.holder_skills, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.tvLevel -> {
                        binding.tvAccountType.text=m
                        binding.rvAccountType.visibility= View.GONE
                        binding.ivDropArrow.rotation = 0f
                    }
                }
            }
        integrityAdapter.list = categoryList
        binding.rvAccountType.adapter = integrityAdapter
    }

    /** api call **/
    private fun accountToggle() {
        val toggleState = when (binding.tvAccountType.text) {
            "Private Account" -> 2
            "Public Account" -> 1
            else -> 1
        }
        val request = HashMap<String, Any>()
        request["type"] = toggleState
        viewModel.accountIntegrityToggle(Constants.PROFILE_SETTING, request)
    }

}