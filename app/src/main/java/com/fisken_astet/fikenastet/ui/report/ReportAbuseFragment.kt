package com.fisken_astet.fikenastet.ui.report

import android.content.Intent
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.utils.BindingUtils
import com.fisken_astet.fikenastet.base.utils.Status
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.api.Constants
import com.fisken_astet.fikenastet.data.model.ReplyModel
import com.fisken_astet.fikenastet.data.model.ReportModel
import com.fisken_astet.fikenastet.databinding.FragmentReportAbuseBinding
import com.fisken_astet.fikenastet.databinding.ItemReportBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportAbuseFragment : BaseFragment<FragmentReportAbuseBinding>() {
    private val viewModel: ReportVM by viewModels()
    private lateinit var reportAdapter: SimpleRecyclerViewAdapter<ReplyModel, ItemReportBinding>
    private var id: String? = null
    private var from: String? = null // 1 user profile  2 post 3 thread 4 catch
    override fun getLayoutResource(): Int {
        return R.layout.fragment_report_abuse
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        // view
        binding.tvTitle.text = "Report Profile"
        id = arguments?.getString("id")
        from = arguments?.getString("from")
        initView()
        // click
        initOnClick()
        // observer
        initObserver()
    }

    private fun initView() {
        initAdapter()
        binding.etReply.movementMethod = ScrollingMovementMethod()
        binding.etReply.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
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

                R.id.consButton, R.id.textButton -> {
                    val selectedPositions = getSelectedPositions()

                    if (selectedPositions.isEmpty()) {
                        showToast("Please add atleast one reason")
                        return@observe
                    }

                    // Check if "others" (last item) is selected
                    val isOthersSelected = selectedPositions.contains(reportAdapter.list.lastIndex+1)
                    val desc = binding.etReply.text.toString().trim()

                    if (isOthersSelected && desc.isEmpty()) {
                        showToast("Please enter description")
                        return@observe
                    }
                    reportApi()
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.reportObserver.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "REPORT" -> {
                            try {
                                val myDataModel = BindingUtils.parseJson<ReportModel>(it.data.toString())
                                if (myDataModel!=null){
                                    if (myDataModel.status==200){
                                        showToast(myDataModel.message.toString())
                                        requireActivity().onBackPressedDispatcher.onBackPressed()
                                    }
                                    else{
                                        showToast(myDataModel.message.toString())
                                    }
                                }

                            } catch (e: Exception) {
                                showToast(e.message.toString())
                                e.printStackTrace()
                            }
                        }
                    }
                }

                Status.ERROR -> {
                    hideLoading()
                    try {
                        showToast(it.message.toString())
                    } catch (e: Exception) {
                        showToast(e.message.toString())
                        e.printStackTrace()
                    }
                }

                else -> {

                }
            }
        }

    }

    // adapter
    private fun initAdapter() {
        reportAdapter = SimpleRecyclerViewAdapter(R.layout.item_report, BR.bean) { v, m, pos ->
            when (v.id) {
                R.id.ivTick -> {
                    if (reportAdapter.list[pos].isSelected == false) {
                        reportAdapter.list[pos].isSelected = true
                        reportAdapter.notifyItemChanged(pos, m)
                    } else {
                        reportAdapter.list[pos].isSelected = false
                        reportAdapter.notifyItemChanged(pos, m)
                    }
                }
            }

        }
        reportAdapter.list = getList()
        binding.rvReply.adapter = reportAdapter
    }


    private fun getList(): ArrayList<ReplyModel> {
        val list = ArrayList<ReplyModel>()
        list.add(ReplyModel("spam"))
        list.add(ReplyModel("rule violations"))
        list.add(ReplyModel("abuse"))
        list.add(ReplyModel("inaccurate"))
        list.add(ReplyModel("offensive"))
        list.add(ReplyModel("off-topic"))
        list.add(ReplyModel("manual"))
        list.add(ReplyModel("others"))
        return list
    }

    private fun getSelectedPositions(): ArrayList<Int> {
        val selectedPositions = ArrayList<Int>()
        reportAdapter.list.forEachIndexed { index, item ->
            if (item.isSelected == true) {
                selectedPositions.add(index + 1)
            }
        }
        return selectedPositions
    }

    private fun reportApi() {
        val request = HashMap<String, Any>()
        if (from != null) {
            when (from) {
                "1" -> {
                    request["type"] = 1
                    request["user_id"] = id.toString()
                }
                "2"->{
                    request["type"] = 2
                    request["post_id"] = id.toString()
                }
                "3"->{
//                    request["type"] = 3
//                    request["user_id"] = id.toString()
                }
                "4"->{
                    request["type"] = 4
                    request["catch_id"] = id.toString()
                }
            }
        }
        request["report_type"] = getSelectedPositions()
        if (getSelectedPositions().contains(reportAdapter.list.lastIndex+1)) {
            request["description"] = binding.etReply.text.toString().trim()
        }
        viewModel.reportApi(Constants.REPORT_USER, request)

    }


}