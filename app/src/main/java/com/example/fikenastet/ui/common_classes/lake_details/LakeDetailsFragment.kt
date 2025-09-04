package com.example.fikenastet.ui.common_classes.lake_details

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.data.model.DummyAllLake
import com.example.fikenastet.databinding.FragmentLakeDetailsBinding
import com.example.fikenastet.databinding.HolderRecentCatchesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LakeDetailsFragment : BaseFragment<FragmentLakeDetailsBinding>() {
    private val viewModel: LakeDetailsVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }


    override fun getLayoutResource(): Int {
        return R.layout.fragment_lake_details
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.textGetStarted -> {
                    findNavController().navigate(R.id.navigateToBuyPermitFragment)

                }
            }
        })

    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyAllLake, HolderRecentCatchesBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_recent_catches, BR.bean) { v, m, pos ->


        }
        binding.rvRecentCatches.adapter = adapter
        adapter.list = cardList
    }

    val cardList = arrayListOf(
        DummyAllLake("4.3kg Pike"), DummyAllLake("4.3kg Pike"), DummyAllLake("4.3kg Pike")


    )


}