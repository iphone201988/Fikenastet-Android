package com.fisken_astet.fikenastet.ui.settings.blocked_user

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
import com.fisken_astet.fikenastet.data.model.BlockedUserList
import com.fisken_astet.fikenastet.data.model.BlockedUserListData
import com.fisken_astet.fikenastet.data.model.GetProfileModel
import com.fisken_astet.fikenastet.databinding.FragmentBlockedUserBinding
import com.fisken_astet.fikenastet.databinding.ItemBlockedUserBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockedUserFragment : BaseFragment<FragmentBlockedUserBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var blockedAdapter: SimpleRecyclerViewAdapter<BlockedUserListData, ItemBlockedUserBinding>
    private var blockedUserList= ArrayList<BlockedUserListData>()
    private var listPosition:Int?=null
    override fun getLayoutResource(): Int {
        return R.layout.fragment_blocked_user
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        // view
        initView()
        // click
        initOnClick()
        // observer
        initObserver()
    }

    /** handle view **/
    private fun initView() {
        getBlockedUser()
        initAdapter()

    }

    /** handle click **/
    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()

                }
                R.id.ivNotification->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }
            }

        }

    }

    /** handle observer **/
    private fun initObserver() {
        viewModel.settingsObserver.observe(viewLifecycleOwner){
            when(it?.status){
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when(it.message){
                        "BLOCKED_USERS"->{
                            try {
                                val myDataModel = BindingUtils.parseJson<BlockedUserList>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        blockedUserList=myDataModel.data as ArrayList<BlockedUserListData>
                                        blockedAdapter.list=blockedUserList
                                    }
                                }
                                handleUi()
                            }
                            catch (e:Exception){
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                        "UNBLOCK_USER"->{
                            try {
                                val myDataModel = BindingUtils.parseJson<GetProfileModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        showToast(myDataModel.message.toString())
                                        blockedUserList.removeAt(listPosition!!)
                                        blockedAdapter.list=blockedUserList
                                        handleUi()
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
                else -> {

                }
            }
        }

    }

    // adapter
    private fun initAdapter() {
        blockedAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_blocked_user, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.tvFollowStatus->{
                        listPosition=pos
                        unblockApi(m.id)
                    }
                }
            }
        blockedAdapter.list = blockedUserList
        binding.rvBlockUsers.adapter = blockedAdapter
    }

    /** api call **/
    private fun getBlockedUser(){
        val request= HashMap<String, Any>()
        request["type"]=5
        viewModel.getBlockedUserList(Constants.RESTRICT_BLOCK,request)
    }

    private fun unblockApi(id: Int?){
        val request = HashMap<String, Any>()
        request["type"]=2
        request["user_id"]=id.toString()
        viewModel.unblockUserApi(Constants.RESTRICT_BLOCK,request)
    }

    private fun handleUi(){
        if (!blockedUserList.isNullOrEmpty()){
            binding.rvBlockUsers.visibility=View.VISIBLE
            binding.tvNoDataFound.visibility=View.GONE
            blockedAdapter.notifyDataSetChanged()
        }
        else{
            binding.rvBlockUsers.visibility=View.GONE
            binding.tvNoDataFound.visibility=View.VISIBLE
        }
    }

}