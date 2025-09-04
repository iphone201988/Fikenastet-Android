package com.example.fikenastet.ui.selling

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseActivity
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.base.utils.BindingUtils
import com.example.fikenastet.data.model.SellDataList
import com.example.fikenastet.databinding.ActivitySellingBinding
import com.example.fikenastet.databinding.HolderSellingBinding
import com.example.fikenastet.ui.dashboard.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellingActivity : BaseActivity<ActivitySellingBinding>() {
    private val viewModel: SellingActivityVM by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.activity_selling
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        setupNavigation()
        initOnClick()
        initView()
        handleRecPos()
    }

    private fun initView() {
        BindingUtils.statusBarStyleBlack(this)
        BindingUtils.styleSystemBars(this, getColor(R.color.black))
        initAdapter()

    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, Observer {
            when (it?.id) {
                R.id.ivBack -> {
                    finish()
                }
            }
        })
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<SellDataList, HolderSellingBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_selling, BR.bean) { v, m, pos ->
            when (v?.id) {
                R.id.tvSale -> {
                    cardList.forEach {
                        it.isSelected = false
                    }
                    m.isSelected = true
                    adapter.notifyDataSetChanged()
                    binding.viewPager.setCurrentItem(pos, false)

                }
            }


        }
        binding.rvSelling.adapter = adapter
        adapter.list = cardList
    }

    val cardList = arrayListOf(
        SellDataList("Inbox", true),
        SellDataList("Your listings", false),
        SellDataList("Messages", false),
        SellDataList("Statistics", false)
    )


    private fun handleRecPos() {
        val margin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 15f, binding.rvSelling.context.resources.displayMetrics
        ).toInt()

        binding.rvSelling.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = state.itemCount

                if (position == 0) {
                    outRect.left = margin // first item
                }

                if (position == itemCount - 1) {
                    outRect.right = margin // last item
                }
            }
        })

    }


    private fun setupNavigation() {
        binding.apply {
            val adapter = ViewPagerSellingAdapter(this@SellingActivity)
            viewPager.adapter = adapter
            viewPager.isUserInputEnabled = false

        }
    }

}