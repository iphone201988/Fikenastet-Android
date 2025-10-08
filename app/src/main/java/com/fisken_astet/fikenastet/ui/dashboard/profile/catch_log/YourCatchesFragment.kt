package com.fisken_astet.fikenastet.ui.dashboard.profile.catch_log

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.databinding.FragmentYourCatchesBinding
import com.fisken_astet.fikenastet.databinding.ItemMyCatchesBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YourCatchesFragment : BaseFragment<FragmentYourCatchesBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    private lateinit var yourCatchesAdapter: SimpleRecyclerViewAdapter<String, ItemMyCatchesBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_your_catches
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    private fun initView() {
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.consButton -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "AddCatchLog")
                    startActivity(intent)
                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        val list = listOf("", "", "", "")
        yourCatchesAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_my_catches, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.ivView, R.id.tvView -> {
                        val intent = Intent(requireActivity(), CommonActivity::class.java)
                        intent.putExtra("fromWhere", "ViewCatchLog")
                        intent.putExtra("catchLog", "0")
                        startActivity(intent)
                    }

                    R.id.ivEdit, R.id.tvEdit -> {

                    }

                    R.id.ivDelete, R.id.tvDelete -> {

                    }
                }
            }
        yourCatchesAdapter.list = list
        binding.rvYourCatches.adapter = yourCatchesAdapter
    }
}