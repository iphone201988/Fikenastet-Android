package com.example.fikenastet.ui.settings.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentAddCardBinding
import com.example.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCardFragment : BaseFragment<FragmentAddCardBinding>() {
    private val viewModel: SettingsVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_card
    }

    override fun getViewModel(): BaseViewModel {
      return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    private fun initView(){

    }

    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{

                }
                R.id.consButton->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun initObserver(){

    }
}