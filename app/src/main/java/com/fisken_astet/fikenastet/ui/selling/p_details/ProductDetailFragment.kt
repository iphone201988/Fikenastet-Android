package com.fisken_astet.fikenastet.ui.selling.p_details

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.data.model.DummyAllLake
import com.fisken_astet.fikenastet.databinding.FragmentProductDetailBinding
import com.fisken_astet.fikenastet.databinding.HolderImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {
    private val viewModel: ProductVM by viewModels()
    override fun onCreateView(view: View) {
        initOnClick()
        initView()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_product_detail
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }

                R.id.consConfirmPurchase -> {
                    findNavController().navigate(R.id.fragmentOrderConfirmed)
                }
            }
        }
    }


    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyAllLake, HolderImageBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_image, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.ivPdf, R.id.tvPdf -> {

                }
            }


        }
        binding.rvImage.adapter = adapter
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