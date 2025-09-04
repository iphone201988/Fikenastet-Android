package com.example.fikenastet.ui.dashboard.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.databinding.FragmentProfileBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileFragmentVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_profile
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

    }

    /** handle click **/
    private fun initOnClick(){
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivNotification->{
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "ThreadDetail")
                    startActivity(intent)
                }
                R.id.ivSettings->{
                    val intent = Intent(requireContext(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "ReportAbuse")
                    startActivity(intent)
                }
            }
        }
    }

    /** handle observer **/
    private fun initObserver(){

    }

}