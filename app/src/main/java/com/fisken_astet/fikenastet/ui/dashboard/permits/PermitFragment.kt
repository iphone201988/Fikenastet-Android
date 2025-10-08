package com.fisken_astet.fikenastet.ui.dashboard.permits

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.utils.BaseCustomBottomSheet
import com.fisken_astet.fikenastet.data.model.DummyPermitData
import com.fisken_astet.fikenastet.databinding.BottomRecipeBinding
import com.fisken_astet.fikenastet.databinding.FragmentPermitBinding
import com.fisken_astet.fikenastet.databinding.HolderActivePermitBinding
import com.fisken_astet.fikenastet.databinding.HolderPreviousPermitBinding
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
                        binding.tvTitle.setText("Inactive Permits")
                        binding.tvPermit.setText("Inactive Permits")
                        initAdapterPrevious()
                    } else {
                        binding.tvPreviousPermit.setText("Previous Permits")
                        binding.tvTitle.setText("Active Permits")
                        binding.tvPermit.setText("Active Permits")
                        initAdapter()
                    }
                }
            }
        })

    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyPermitData, HolderActivePermitBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_active_permit, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.ivPdf, R.id.tvPdf -> {
                    bottomSheetCommon.show()
                }
                R.id.tvLakeInfo,R.id.ivLakeInfo->{
                    adapter.list[pos].isSelected=true
                    adapter.notifyItemChanged(pos,null)
                }
                R.id.tvMinimise->{
                    adapter.list[pos].isSelected=false
                    adapter.notifyItemChanged(pos,null)
                }
            }


        }
        binding.rvActivePermit.adapter = adapter
        adapter.list = cardList
    }

    val cardList = arrayListOf(
        DummyPermitData("Vättern lake"), DummyPermitData("Vättern lake")
    )


    private lateinit var adapterPrevious: SimpleRecyclerViewAdapter<DummyPermitData, HolderPreviousPermitBinding>
    private fun initAdapterPrevious() {
        adapterPrevious =
            SimpleRecyclerViewAdapter(R.layout.holder_previous_permit, BR.bean) { v, m, pos ->
                when (v?.id) {
                    R.id.ivPdf, R.id.tvPdf -> {
                        bottomSheetCommon.show()
                    }
                    R.id.tvLakeInfo,R.id.ivLakeInfo->{
                        adapterPrevious.list[pos].isSelected=true
                        adapterPrevious.notifyItemChanged(pos,null)
                    }
                    R.id.tvMinimise->{
                        adapterPrevious.list[pos].isSelected=false
                        adapterPrevious.notifyItemChanged(pos,null)
                    }
                }

            }
        binding.rvActivePermit.adapter = adapterPrevious
        adapterPrevious.list = cardList
    }

    private lateinit var bottomSheetCommon: BaseCustomBottomSheet<BottomRecipeBinding>
    private fun genderBottomSheet() {
        bottomSheetCommon = BaseCustomBottomSheet(requireActivity(), R.layout.bottom_recipe,R.style.SheetDialog2) {}
        bottomSheetCommon.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetCommon.behavior.isDraggable = true
        bottomSheetCommon.setCancelable(true)
        bottomSheetCommon.create()


    }


}