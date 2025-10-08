package com.fisken_astet.fikenastet.ui.dashboard.permits.buy_permit

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentBuyPermitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyPermitFragment : BaseFragment<FragmentBuyPermitBinding>() {
    private val viewModel: BuyPermitVM by viewModels()
    override fun onCreateView(view: View) {
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_buy_permit
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressed()
                }

                R.id.consPay -> {
                    findNavController().navigate(R.id.navigateToPermitPurchasedFragment)
                }
            }
        })
    }

}