package com.fisken_astet.fikenastet.ui.dashboard.all_lake

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.data.model.DummyAllLake
import com.fisken_astet.fikenastet.databinding.FragmentAllLakeBinding
import com.fisken_astet.fikenastet.databinding.HolderAllLakeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllLakeFragment : BaseFragment<FragmentAllLakeBinding>() {
    private val viewModel: AllLakeVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }


    override fun getLayoutResource(): Int {
        return R.layout.fragment_all_lake
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
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        })

    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyAllLake, HolderAllLakeBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_all_lake, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivMoveDetails -> {
                    findNavController().navigate(R.id.fragmentLakeDetails)
                }
            }


        }
        binding.rvAllLake.adapter = adapter
        adapter.list = cardList
    }

    val cardList = arrayListOf(
        DummyAllLake("Lake Mälaren"),
        DummyAllLake("Lake Mälaren"),
        DummyAllLake("Lake Mälaren"),
        DummyAllLake("Lake Mälaren"),
        DummyAllLake("Lake Mälaren"),
        DummyAllLake("Lake Mälaren")


    )

}