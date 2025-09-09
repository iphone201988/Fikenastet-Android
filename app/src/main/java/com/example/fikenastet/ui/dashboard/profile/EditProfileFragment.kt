package com.example.fikenastet.ui.dashboard.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {
    private val viewModel: ProfileFragmentVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_edit_profile
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView(view: View) {
        initView()
        initObserver()
        initOnClick()
    }

    private fun initView(){

    }

    private fun initObserver(){

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

}