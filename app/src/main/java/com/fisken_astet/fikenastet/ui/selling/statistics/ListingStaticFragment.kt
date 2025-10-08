package com.fisken_astet.fikenastet.ui.selling.statistics

import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentListingStaticBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingStaticFragment : BaseFragment<FragmentListingStaticBinding>() {
    private val viewModel: StatisticsVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_listing_static
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView(view: View) {
       // view
        initView()
        // click
        initOnClick()
        // observer
        initObserver()
    }

    /** handle view **/
    private fun initView(){
        binding.circularProgress.animateProgress(value = 208f, maxValue = 1000f)
    }

    /** handle click **/
    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

    }

    /** handle observer **/
    private fun initObserver(){

    }

}