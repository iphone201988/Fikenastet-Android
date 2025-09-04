package com.example.fikenastet.ui.selling.order_confirmed

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentOrderConfirmedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderConfirmedFragment : BaseFragment<FragmentOrderConfirmedBinding>() {
    private val viewModel: OrderConfirmedVM by viewModels()
    override fun onCreateView(view: View) {
        initOnClick()
        initView()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_order_confirmed
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.consConfirmPurchase -> {
                    findNavController().navigate(R.id.fragmentTrackOrder)
                }

                R.id.ivBack -> {
                    findNavController().popBackStack()
                }
            }
        })

    }


}