package com.example.fikenastet.ui.dashboard.marketplace

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.DummyAllLake
import com.example.fikenastet.databinding.FragmentMarketplaceBinding
import com.example.fikenastet.databinding.HolderMarketplaceBinding
import com.example.fikenastet.ui.selling.SellingActivity
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
            "Shimano\n" + "Reel 3000 XT"
        ), DummyAllLake(
            "Shimano\n" + "Reel 3000 XT"
        ), DummyAllLake(
            "Shimano\n" + "Reel 3000 XT"
        )
    )

}