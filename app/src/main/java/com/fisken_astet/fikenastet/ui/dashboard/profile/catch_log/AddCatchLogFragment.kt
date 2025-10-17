package com.fisken_astet.fikenastet.ui.dashboard.profile.catch_log

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentAddCatchLogBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCatchLogFragment : BaseFragment<FragmentAddCatchLogBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_catch_log
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

    }

    private fun initObserver() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }

                R.id.ivDropArrow->{

                }
                R.id.ivDropArrow2->{

                }
                R.id.ivDropArrow3->{

                }
                R.id.tvAddMore->{

                }
                R.id.consUploadPic->{

                }
                R.id.consButton->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

}