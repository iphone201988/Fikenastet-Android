package com.example.fikenastet.ui.selling.your_listings

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
import com.example.fikenastet.databinding.FragmentYourListingBinding
import com.example.fikenastet.databinding.HolderMarketplaceBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
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

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyAllLake, HolderMarketplaceBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_marketplace, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.tvViewDetails -> {
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "ProductDetailFragment")
                    startActivity(intent)


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