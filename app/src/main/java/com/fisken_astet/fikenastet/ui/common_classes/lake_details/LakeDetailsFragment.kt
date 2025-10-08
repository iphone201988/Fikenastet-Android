package com.fisken_astet.fikenastet.ui.common_classes.lake_details

import android.content.Intent
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
import com.fisken_astet.fikenastet.databinding.FragmentLakeDetailsBinding
import com.fisken_astet.fikenastet.databinding.HolderRecentCatchesBinding
import com.fisken_astet.fikenastet.ui.dashboard.DashBoardActivity
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
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.consLoginButton -> {
                    findNavController().navigate(R.id.navigateToBuyPermitFragment)

                }

                R.id.consMapView->{
                    val intent = Intent(requireContext(), DashBoardActivity::class.java)
                    intent.putExtra("from", "map")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    requireActivity().finish()
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