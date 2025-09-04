package com.example.fikenastet.ui.dashboard.home

import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.base.utils.BaseCustomBottomSheet
import com.example.fikenastet.data.model.DummyComment
import com.example.fikenastet.data.model.DummyHomeCard
import com.example.fikenastet.data.model.DummySocialFeed
import com.example.fikenastet.databinding.CommonBottomLayoutBinding
import com.example.fikenastet.databinding.FragmentHomeBinding
import com.example.fikenastet.databinding.HolderHomeCardBinding
import com.example.fikenastet.databinding.HolderSocialFeedBinding
import com.example.fikenastet.databinding.ItemCommentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeFragmentVM by viewModels()
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


    private fun initOnClick() {

    }

    private fun initView() {
        initAdapter()
        initAdapterSocialFeed()
        genderBottomSheet()
        initAdapterComment()
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<DummyHomeCard, HolderHomeCardBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(R.layout.holder_home_card, BR.bean) { v, m, pos ->


        }
        binding.rvCardHome.adapter = adapter
        adapter.list = cardList
    }

    val cardList = arrayListOf(
        DummyHomeCard(1, "5 hottest topics today", R.drawable.fish),
        DummyHomeCard(2, "5 most liked posts today", R.drawable.fav),
        DummyHomeCard(3, "5 latest catches", R.drawable.fish_rev),
//        DummyHomeCard(4, "Forum", R.drawable.iv_chat),
        DummyHomeCard(4, "Information", R.drawable.infor)
    )

    private lateinit var adapterSocialFeed: SimpleRecyclerViewAdapter<DummySocialFeed, HolderSocialFeedBinding>
    private fun initAdapterSocialFeed() {
        adapterSocialFeed =
            SimpleRecyclerViewAdapter(R.layout.holder_social_feed, BR.bean) { v, m, _ ->
                when (v?.id) {
                    R.id.ivChat, R.id.tvChatCount -> {
                        bottomSheetCommon.show()
                    }
                }

            }
        binding.rvSocialFeed.adapter = adapterSocialFeed
        adapterSocialFeed.list = cardListSocialFeed
    }

    val cardListSocialFeed = arrayListOf(
        DummySocialFeed("William"), DummySocialFeed("DvSam")

    )


    private lateinit var bottomSheetCommon: BaseCustomBottomSheet<CommonBottomLayoutBinding>
    private fun genderBottomSheet() {
        bottomSheetCommon =
            BaseCustomBottomSheet(requireActivity(), R.layout.common_bottom_layout) {}
        bottomSheetCommon.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetCommon.behavior.isDraggable = true
        bottomSheetCommon.setCancelable(true)
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


}
