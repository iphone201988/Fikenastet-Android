package com.example.fikenastet.ui.dashboard.all_lake

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
import com.example.fikenastet.databinding.FragmentAllLakeBinding
import com.example.fikenastet.databinding.HolderAllLakeBinding
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