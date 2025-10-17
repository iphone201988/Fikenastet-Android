package com.fisken_astet.fikenastet.ui.dashboard.common_activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseActivity
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.databinding.ActivityCommonBinding
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
                        val message = intent.getStringExtra("fromWhere")
                        if (message != null) {
                            val bundle = Bundle()
                            when (message) {
                                "MapSeeAll" -> {
                                    setStartDestination(R.id.fragmentAllLake)
                                }

                                "ProductDetailFragment" -> {
                                    setStartDestination(R.id.fragmentProductDetail)
                                }

                                "PostNewListing" -> {
                                    setStartDestination(R.id.fragmentPostNewListing)
                                }
//                                "BuyPermit"->{
//                                    setStartDestination(R.id.fragmentBuyPermit)
//                                }
//                                "PermitPurchased"->{
//                                    setStartDestination(R.id.fragmentPermitPurchased)
//                                }


                                "ThreadDetail" -> {
                                    setStartDestination(R.id.fragmentThreadDetail)
                                }

                                "NewThread" -> {
                                    setStartDestination(R.id.fragmentNewThread)
                                }

                                "AllThreads" -> {
                                    setStartDestination(R.id.fragmentAllThreads)
                                }

                                "ReportAbuse" -> {
                                    //from  1 user profile  2 post 3 thread 4 catch
                                    val userId = intent.getStringExtra("id")
                                    val from = intent.getStringExtra("from")
                                    bundle.putString("id", userId)
                                    bundle.putString("from", from)
                                    setStartDestination(R.id.fragmentReportAbuse)
                                }

                                "ChangePassword" -> {
                                    setStartDestination(R.id.fragmentChangePassword)
                                }

                                "NotificationsEnabled" -> {
                                    setStartDestination(R.id.fragmentNotificationsEnabled)
                                }

                                "Payment" -> {
                                    setStartDestination(R.id.fragmentPayment)
                                }

                                "AddCard" -> {
                                    setStartDestination(R.id.fragmentAddCard)
                                }

                                "EditProfile" -> {
                                    setStartDestination(R.id.fragmentEditProfile)
                                }

                                "Followers" -> {
                                    val userId = intent.getStringExtra("userId")
                                    bundle.putString("userId", userId)
                                    setStartDestination(R.id.fragmentFollower)
                                }

                                "Following" -> {
                                    val userId = intent.getStringExtra("userId")
                                    bundle.putString("userId", userId)
                                    setStartDestination(R.id.fragmentFollowing)
                                }

                                "MyLakes" -> {
                                    setStartDestination(R.id.fragmentMyLakes)
                                }

                                "MyLakeDetail" -> {
                                    setStartDestination(R.id.fragmentMyLakeDetail)
                                }

                                "AddNewLake" -> {
                                    setStartDestination(R.id.fragmentAddNewLake)
                                }

                                "AddFeed" -> {
                                    setStartDestination(R.id.fragmentAddFeed)
                                }

                                "MessageList" -> {
                                    setStartDestination(R.id.fragmentMessageList)
                                }

                                "OtherProfile" -> {
                                    val userId = intent.getStringExtra("id")
                                    bundle.putString("id", userId)
                                    setStartDestination(R.id.fragmentOtherUser)
                                }

                                "CatchLog" -> {
                                    setStartDestination(R.id.fragmentCatchLog)
                                }

                                "AddCatchLog" -> {
                                    setStartDestination(R.id.fragmentAddCatchLog)
                                }

                                "ViewCatchLog" -> {
                                    val catchLog = intent.getStringExtra("catchLog")
                                    Log.e("dsdsdsdsd", "initView: $catchLog", )
                                    bundle.putString("catchLog", catchLog)
                                    setStartDestination(R.id.fragmentViewCatchLog)
                                }

                                "Notifications" -> {
                                    setStartDestination(R.id.fragmentNotifications)
                                }

                                "BlockedUser" -> {
                                    setStartDestination(R.id.fragmentBlockedUser)
                                }

                                "AccountIntegrity" -> {
                                    setStartDestination(R.id.fragmentAccountIntegrity)
                                }

                                "2FA" -> {
                                    setStartDestination(R.id.fragmentTwoFactorAuth)
                                }

                                "ListingStatistic" -> {
                                    setStartDestination(R.id.fragmentListingStatic)
                                }
                                "DevicePermissions"->{
                                    setStartDestination(R.id.fragmentDevicePermissions)
                                }
                            }
                            navController.setGraph(this, bundle)
                        }
                    }
            }
        }
    }
}