package com.example.fikenastet.ui.dashboard.profile.catch_log

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentYourCatchesBinding
import com.example.fikenastet.databinding.ItemMyCatchesBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
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
                    R.id.ivHeart, R.id.tvView -> {
                        val intent = Intent(requireActivity(), CommonActivity::class.java)
                        intent.putExtra("fromWhere", "ViewCatchLog")
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