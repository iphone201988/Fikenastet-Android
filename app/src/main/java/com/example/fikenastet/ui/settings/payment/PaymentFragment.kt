package com.example.fikenastet.ui.settings.payment

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.fikenastet.BR
import com.example.fikenastet.R
import com.example.fikenastet.base.BaseFragment
import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.SimpleRecyclerViewAdapter
import com.example.fikenastet.databinding.FragmentPaymentBinding
import com.example.fikenastet.databinding.ItemCardsBinding
import com.example.fikenastet.databinding.ItemPaymentHistoryBinding
import com.example.fikenastet.ui.dashboard.common_activity.CommonActivity
import com.example.fikenastet.ui.settings.SettingsVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {
    private val viewModel: SettingsVM by viewModels()
    private lateinit var historyAdapter: SimpleRecyclerViewAdapter<String, ItemPaymentHistoryBinding>
    private lateinit var cardsAdapter: SimpleRecyclerViewAdapter<String, ItemCardsBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_payment
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
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()

                }
                R.id.ivNotification->{

                }
                R.id.tvLoadMore->{
                    historyAdapter.list.addAll(listOf("","",""))
                    historyAdapter.notifyDataSetChanged()

                }
                R.id.consButton2->{
                    val intent= Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "AddCard")
                    startActivity(intent)
                }
                R.id.consButton->{

                }
            }
        }

    }

    private fun initObserver() {

    }

    private fun initAdapter() {
        val historyList = listOf("", "", "")
        historyAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_payment_history, BR.bean) { v, m, pos ->
            }
        historyAdapter.list = historyList
        binding.rvHistory.adapter = historyAdapter

        val cardsList = listOf("")
        cardsAdapter = SimpleRecyclerViewAdapter(R.layout.item_cards, BR.bean) { v, m, pos ->
        }
        cardsAdapter.list = cardsList
        binding.rvCards.adapter = cardsAdapter

    }

}