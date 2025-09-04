package com.example.fikenastet.ui.dashboard.common_activity

import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseActivity
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.utils.BindingUtils
import com.example.fikenastet.databinding.ActivityCommonBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommonActivity : BaseActivity<ActivityCommonBinding>() {
    private val viewModel: CommonActivityVM by viewModels()

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHost) as NavHostFragment).navController
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_common
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        initView()
        initOnClick()
    }

    private fun initOnClick() {

    }

    private fun initView() {
        BindingUtils.statusBarStyleBlack(this)
        BindingUtils.styleSystemBars(this, getColor(R.color.black))
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                navController.graph =
                    navController.navInflater.inflate(R.navigation.common_navigation).apply {
                        val formWhere = intent.getStringExtra("fromWhere")
                        when (formWhere) {
                            "MapSeeAll" -> {
                                setStartDestination(R.id.fragmentAllLake)
                            }

                            "ProductDetailFragment" -> {
                                setStartDestination(R.id.fragmentProductDetail)
                            }

                            "PostNewListing" -> {
                                setStartDestination(R.id.fragmentPostNewListing)
                            }



                            "ThreadDetail"->{
                                setStartDestination(R.id.fragmentThreadDetail)
                            }
                            "NewThread"->{
                                setStartDestination(R.id.fragmentNewThread)
                            }
                            "ReportAbuse"->{
                                setStartDestination(R.id.fragmentReportAbuse)
                            }
                        }
                    }
            }
        }
    }
}