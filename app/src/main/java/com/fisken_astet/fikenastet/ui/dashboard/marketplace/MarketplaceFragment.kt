package com.fisken_astet.fikenastet.ui.dashboard.marketplace

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.data.model.DummyAllLake
import com.fisken_astet.fikenastet.databinding.FragmentMarketplaceBinding
import com.fisken_astet.fikenastet.databinding.HolderMarketplaceBinding
import com.fisken_astet.fikenastet.ui.selling.SellingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketplaceFragment : BaseFragment<FragmentMarketplaceBinding>() {
    private val viewmodel: MarketPlaceVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_marketplace
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

    private fun initOnClick() {
        viewmodel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.tvSale -> {
                    val intent = Intent(requireContext(), SellingActivity::class.java)
                    startActivity(intent)
                }
            }
        })

    }

    private fun initView() {
        initAdapter()
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyAllLake, HolderMarketplaceBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_marketplace, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.ivPdf, R.id.tvPdf -> {

                }
            }


        }
        binding.rvMarketplace.adapter = adapter
        adapter.list = cardList
    }

    val cardList = arrayListOf(
        DummyAllLake(
            "Shimano Reel 3000 XT"
        ), DummyAllLake(
            "Shimano Reel 3000 XT"
        ), DummyAllLake(
            "Shimano Reel 3000 XT"
        )
    )

}