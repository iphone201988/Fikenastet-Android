package com.fisken_astet.fikenastet.ui.selling.your_listings

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
import com.fisken_astet.fikenastet.databinding.FragmentYourListingBinding
import com.fisken_astet.fikenastet.databinding.HolderListingItemViewBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YourListingFragment : BaseFragment<FragmentYourListingBinding>() {
    private val viewModel: YourListingVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        intiOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_your_listing
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun intiOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {

            }
        })
    }

    private fun initView() {
        initAdapter()
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyAllLake, HolderListingItemViewBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_listing_item_view, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.consViewDetail -> {
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "ProductDetailFragment")
                    startActivity(intent)


                }

                // remove
                R.id.tvViewDetails->{

                }

                // edit
                R.id.tvBuyNow->{

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