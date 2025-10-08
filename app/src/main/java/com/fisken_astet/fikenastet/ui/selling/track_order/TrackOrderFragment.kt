package com.fisken_astet.fikenastet.ui.selling.track_order

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.databinding.FragmentTrackOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackOrderFragment : BaseFragment<FragmentTrackOrderBinding>() {
    private val viewModel: TrackOrderVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_track_order
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private fun initView() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.ivBack -> {
                    findNavController().popBackStack()
                }
            }
        })

    }

}