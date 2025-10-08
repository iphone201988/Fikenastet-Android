package com.fisken_astet.fikenastet.ui.dashboard.profile.catch_log

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.utils.BaseCustomBottomSheet
import com.fisken_astet.fikenastet.base.utils.BaseCustomDialog
import com.fisken_astet.fikenastet.data.model.DummyComment
import com.fisken_astet.fikenastet.databinding.CommonBottomLayoutBinding
import com.fisken_astet.fikenastet.databinding.DialogDeleteCatchBinding
import com.fisken_astet.fikenastet.databinding.FragmentViewCatchReportBinding
import com.fisken_astet.fikenastet.databinding.ItemCommentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewCatchReportFragment : BaseFragment<FragmentViewCatchReportBinding>() {
    private val viewModel: CatchLogVM by viewModels()
    // adapter
//    private lateinit var replyAdapter: SimpleRecyclerViewAdapter<ReplyModel, ItemReplyBinding>

    // dialog
    private lateinit var deleteCatchLogDialog: BaseCustomDialog<DialogDeleteCatchBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_view_catch_report
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
//        initAdapter()
        val catchLog=arguments?.getString("catchLog")
        Log.e("hghghghgg", "initView: $catchLog", )
        if (catchLog=="1"){
            binding.consButton3.visibility=View.VISIBLE
            binding.consButtons.visibility=View.GONE
        }
        else{
            binding.consButton3.visibility=View.GONE
            binding.consButtons.visibility=View.VISIBLE
        }
        genderBottomSheet()
        initAdapterComment()
        initDialog()
    }

    private fun initObserver() {

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.ivBack -> {
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }

                R.id.ivNotification -> {

                }
                R.id.consButton2->{
                    deleteCatchLogDialog.show()
                }
                R.id.consButton -> {

                }

                // comments
                R.id.consButton3->{
                    bottomSheetCommon.show()
                }
            }
        }
    }

    // adapter
//    private fun initAdapter() {
//        replyAdapter = SimpleRecyclerViewAdapter(R.layout.item_reply, BR.bean) { v, m, pos ->
//            when (v.id) {
//                R.id.ivHeart -> {
//                    if (replyAdapter.list[pos].isSelected==false) {
//                        replyAdapter.list[pos].isSelected = true
//                        replyAdapter.notifyItemChanged(pos,m)
//                    }
//                    else{
//                        replyAdapter.list[pos].isSelected = false
//                        replyAdapter.notifyItemChanged(pos,m)
//                    }
//                }
//
//            }
//        }
//        replyAdapter.list = getReplyList()
//        binding.rvReply.adapter = replyAdapter
//    }

//    private fun getReplyList(): ArrayList<ReplyModel> {
//        val list = ArrayList<ReplyModel>()
//        list.add(ReplyModel(""))
//        list.add(ReplyModel(""))
//        list.add(ReplyModel(""))
//        list.add(ReplyModel(""))
//        list.add(ReplyModel(""))
//        list.add(ReplyModel(""))
//        return list
//    }

    private lateinit var bottomSheetCommon: BaseCustomBottomSheet<CommonBottomLayoutBinding>
    private fun genderBottomSheet() {
        bottomSheetCommon =
            BaseCustomBottomSheet(requireActivity(), R.layout.common_bottom_layout,R.style.SheetDialog2) {}
        bottomSheetCommon.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetCommon.behavior.isDraggable = true
        bottomSheetCommon.behavior.peekHeight=200
        bottomSheetCommon.setCancelable(true)
        bottomSheetCommon.binding.apply {
            consChat.visibility=View.GONE
            edtMessage.visibility=View.GONE
            ivSend.visibility=View.GONE
        }
        bottomSheetCommon.create()


    }


    private lateinit var adapterComment: SimpleRecyclerViewAdapter<DummyComment, ItemCommentBinding>
    private fun initAdapterComment() {
        adapterComment =
            SimpleRecyclerViewAdapter(R.layout.item_comment, BR.bean) { view, value, _ ->
                /*if (view.id == R.id.consMain) {
                    binding.etGender.setText(value)
                    bottomSheetCommon.dismiss()
                }*/
            }
        bottomSheetCommon.binding.rvComment.adapter = adapterComment
        adapterComment.list = commentList
    }

    val commentList = arrayListOf(
        DummyComment("Amazing!!!"),
        DummyComment("Good"),
        DummyComment("Amazing"),
        DummyComment("Nice Fish")

    )

    private fun initDialog(){
        deleteCatchLogDialog = BaseCustomDialog(requireContext(),R.layout.dialog_delete_catch){
            when(it.id){
                R.id.consButton->{
                    deleteCatchLogDialog.dismiss()
                }
                R.id.consButton2->{
                    deleteCatchLogDialog.dismiss()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

}