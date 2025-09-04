package com.example.fikenastet.ui.dashboard.permits.purchased

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentPermitPurchasedBinding
import com.example.fikenastet.ui.dashboard.DashBoardActivity
import com.example.fikenastet.ui.dashboard.permits.buy_permit.BuyPermitVM
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
                    startActivity(intent)
                }
            }

        }
    }
}