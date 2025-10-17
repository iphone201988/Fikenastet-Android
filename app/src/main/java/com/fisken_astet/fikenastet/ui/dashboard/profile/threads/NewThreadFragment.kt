package com.fisken_astet.fikenastet.ui.dashboard.profile.threads

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.databinding.FragmentNewThreadBinding
import com.fisken_astet.fikenastet.databinding.ItemThreadsCategoryBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewThreadFragment : BaseFragment<FragmentNewThreadBinding>() {
    private val viewModel: ThreadsVM by viewModels()
    private lateinit var categoryAdapter: SimpleRecyclerViewAdapter<String, ItemThreadsCategoryBinding>
    private var isClicked:Boolean=false
    override fun getLayoutResource(): Int {
        return R.layout.fragment_new_thread
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
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }

                R.id.ivDropArrow -> {
                    isClicked = !isClicked
                    binding.rvCategory.visibility = if (isClicked) View.VISIBLE else View.GONE
                    binding.ivDropArrow.rotation = if (isClicked) 180f else 0f
                }

                R.id.consButton,R.id.textButton -> {

                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        val categoryList = listOf(
            "Techniques & Tips",
            "Catch Reports",
            "Gear Reviews",
            "Fishing Spots",
            "Rules & Regulations",
            "Help & Questions"
        )
        categoryAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_threads_category, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.clMain -> {
                        binding.tvCategory.text=m
                        binding.rvCategory.visibility= View.GONE
                        binding.ivDropArrow.rotation = 0f
                    }
                }
            }
        categoryAdapter.list = categoryList
        binding.rvCategory.adapter = categoryAdapter
    }

}