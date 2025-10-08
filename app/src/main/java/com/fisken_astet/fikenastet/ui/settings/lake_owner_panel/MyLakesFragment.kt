package com.fisken_astet.fikenastet.ui.settings.lake_owner_panel

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.data.model.MyLakesModel
import com.fisken_astet.fikenastet.databinding.FragmentMyLakesBinding
import com.fisken_astet.fikenastet.databinding.ItemMyLakesBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyLakesFragment : BaseFragment<FragmentMyLakesBinding>() {
    private val viewModel: MyLakesVM by viewModels()
    private lateinit var myLakesAdapter: SimpleRecyclerViewAdapter<MyLakesModel, ItemMyLakesBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_my_lakes
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
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {

                }

                R.id.consButton -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "AddNewLake")
                    startActivity(intent)
                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        myLakesAdapter = SimpleRecyclerViewAdapter(R.layout.item_my_lakes, BR.bean) { v, m, pos ->
            when (v.id) {
                R.id.clMain -> {
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "MyLakeDetail")
                    startActivity(intent)
                }

                R.id.consEdit -> {

                }

                R.id.consDelete -> {

                }
            }
        }
        myLakesAdapter.list = getList()
        binding.rvLakes.adapter = myLakesAdapter

    }

    private fun getList(): ArrayList<MyLakesModel> {
        val list = ArrayList<MyLakesModel>()
        list.add(MyLakesModel("Lake Mälaren", "Under Review", "12", "$500"))
        list.add(MyLakesModel("Lake Mälaren", "Approved", "12", "$1200"))
        list.add(MyLakesModel("Lake Mälaren", "Approved", "12", "$1200"))
        return list
    }
}