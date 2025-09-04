package com.example.fikenastet.ui.selling.p_details

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.DummyAllLake
import com.example.fikenastet.databinding.FragmentProductDetailBinding
import com.example.fikenastet.databinding.HolderImageBinding
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