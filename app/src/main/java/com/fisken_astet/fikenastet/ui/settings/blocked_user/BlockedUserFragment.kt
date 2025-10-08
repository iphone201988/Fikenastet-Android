package com.fisken_astet.fikenastet.ui.settings.blocked_user

import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.databinding.FragmentBlockedUserBinding
import com.fisken_astet.fikenastet.databinding.ItemBlockedUserBinding
import com.fisken_astet.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockedUserFragment : BaseFragment<FragmentBlockedUserBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var blockedAdapter: SimpleRecyclerViewAdapter<String, ItemBlockedUserBinding>
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

                }
            }

        }

    }

    /** handle observer **/
    private fun initObserver() {

    }

    // adapter
    private fun initAdapter() {
        blockedAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_blocked_user, BR.bean) { v, m, pos ->
                when (v.id) {

                }
            }
        blockedAdapter.list = listOf<String>("", "", "")
        binding.rvBlockUsers.adapter = blockedAdapter
    }

}