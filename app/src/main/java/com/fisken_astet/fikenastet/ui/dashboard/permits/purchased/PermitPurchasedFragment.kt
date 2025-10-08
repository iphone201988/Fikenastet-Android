package com.fisken_astet.fikenastet.ui.dashboard.permits.purchased

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentPermitPurchasedBinding
import com.fisken_astet.fikenastet.ui.dashboard.DashBoardActivity
import com.fisken_astet.fikenastet.ui.dashboard.permits.buy_permit.BuyPermitVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermitPurchasedFragment : BaseFragment<FragmentPermitPurchasedBinding>() {
    private val viewModel: BuyPermitVM by viewModels()
    override fun onCreateView(view: View) {
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_permit_purchased
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) { view ->
            when (view?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressed()
                }

                R.id.consAllPermit -> {
                    val intent = Intent(requireContext(), DashBoardActivity::class.java)
                    intent.putExtra("from", "purchased")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

        }
    }
}