package com.example.fikenastet.ui.dashboard.permits

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.base.utils.BaseCustomBottomSheet
import com.example.fikenastet.data.model.DummyAllLake
import com.example.fikenastet.databinding.BottomRecipeBinding
import com.example.fikenastet.databinding.FragmentPermitBinding
import com.example.fikenastet.databinding.HolderActivePermitBinding
import com.example.fikenastet.databinding.HolderPreviousPermitBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermitFragment : BaseFragment<FragmentPermitBinding>() {
    private val viewModel: PermitFragmentVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_permit
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {
        initAdapter()
        genderBottomSheet()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.consPreviousPermit -> {
                    if (binding.tvPreviousPermit.text.toString().equals("Previous Permits")) {
                        binding.tvPreviousPermit.setText("Active Permits")
                        binding.tvTitle.setText("Previous Permits")
                        initAdapterPrevious()
                    } else {
                        binding.tvPreviousPermit.setText("Previous Permits")
                        binding.tvTitle.setText("Active Permits")
                        initAdapter()
                    }
                }
            }
        })

    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyAllLake, HolderActivePermitBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_active_permit, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.ivPdf, R.id.tvPdf -> {
                    bottomSheetCommon.show()
                }
            }


        }
        binding.rvActivePermit.adapter = adapter
        adapter.list = cardList
    }

    val cardList = arrayListOf(
        DummyAllLake("Vättern lake"), DummyAllLake("Vättern lake")
    )


    private lateinit var adapterPrevious: SimpleRecyclerViewAdapter<DummyAllLake, HolderPreviousPermitBinding>
    private fun initAdapterPrevious() {
        adapterPrevious =
            SimpleRecyclerViewAdapter(R.layout.holder_previous_permit, BR.bean) { v, m, pos ->
                when (v?.id) {
                    R.id.ivPdf, R.id.tvPdf -> {
                        bottomSheetCommon.show()
                    }
                }

            }
        binding.rvActivePermit.adapter = adapterPrevious
        adapterPrevious.list = cardList
    }

    private lateinit var bottomSheetCommon: BaseCustomBottomSheet<BottomRecipeBinding>
    private fun genderBottomSheet() {
        bottomSheetCommon = BaseCustomBottomSheet(requireActivity(), R.layout.bottom_recipe) {}
        bottomSheetCommon.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetCommon.behavior.isDraggable = true
        bottomSheetCommon.setCancelable(true)
        bottomSheetCommon.create()


    }


}