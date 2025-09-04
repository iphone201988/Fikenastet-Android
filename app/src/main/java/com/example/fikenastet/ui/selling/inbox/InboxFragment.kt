package com.example.fikenastet.ui.selling.inbox

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentInboxBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InboxFragment : BaseFragment<FragmentInboxBinding>() {
    private val viewModel: InboxVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_inbox
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.consLoginButton -> {
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "PostNewListing")
                    startActivity(intent)
                }

                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        })

    }

}