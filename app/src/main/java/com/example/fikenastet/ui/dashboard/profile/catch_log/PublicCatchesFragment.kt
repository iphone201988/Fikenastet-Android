package com.example.fikenastet.ui.dashboard.profile.catch_log

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentPublicCatchesBinding
import com.example.fikenastet.databinding.ItemMyCatchesBinding
import com.example.fikenastet.databinding.ItemPublicCatchesBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublicCatchesFragment : BaseFragment<FragmentPublicCatchesBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    private lateinit var publicCatchesAdapter: SimpleRecyclerViewAdapter<String, ItemPublicCatchesBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_public_catches
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

            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        val list = listOf("", "", "", "")
        publicCatchesAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_public_catches, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.clMain->{
                        val intent = Intent(requireActivity(), CommonActivity::class.java)
                        intent.putExtra("fromWhere", "ViewCatchLog")
                        startActivity(intent)
                    }
                }
            }
        publicCatchesAdapter.list = list
        binding.rvPublicCatches.adapter = publicCatchesAdapter
    }

}