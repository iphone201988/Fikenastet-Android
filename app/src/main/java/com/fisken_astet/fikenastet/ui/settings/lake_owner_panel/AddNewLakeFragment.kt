package com.fisken_astet.fikenastet.ui.settings.lake_owner_panel

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.fisken_astet.fikenastet.BR
import com.fisken_astet.fikenastet.R
import com.fisken_astet.fikenastet.base.BaseFragment
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.SimpleRecyclerViewAdapter
import com.fisken_astet.fikenastet.base.utils.showToast
import com.fisken_astet.fikenastet.data.model.ReplyModel
import com.fisken_astet.fikenastet.databinding.FragmentAddNewLakeBinding
import com.fisken_astet.fikenastet.databinding.ItemAddRulesBinding
import com.fisken_astet.fikenastet.databinding.ItemReportBinding
import com.fisken_astet.fikenastet.ui.dashboard.common_activity.CommonActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewLakeFragment : BaseFragment<FragmentAddNewLakeBinding>() {
    private val viewModel: MyLakesVM by viewModels()
    private lateinit var addRulesAdapter: SimpleRecyclerViewAdapter<String, ItemAddRulesBinding>
    private lateinit var addHotspotAdapter: SimpleRecyclerViewAdapter<String, ItemAddRulesBinding>
    private lateinit var addSpeciesAdapter: SimpleRecyclerViewAdapter<ReplyModel, ItemReportBinding>
    private var selectedSpeciesList = ArrayList<String>()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_new_lake
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView(view: View) {
        iniView()
        initOnClick()
        initObserver()
    }

    private fun iniView() {
        initAdapter()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner){
            when(it?.id){
                R.id.ivBack->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                R.id.ivNotification->{
                    val intent = Intent(requireActivity(), CommonActivity::class.java)
                    intent.putExtra("fromWhere", "Notifications")
                    startActivity(intent)
                }
                R.id.tvAddMore->{

                }
                R.id.consUploadPic->{

                }
                // season
                R.id.ivDropArrow->{

                }
                // permit
                R.id.ivDropArrow2->{

                }
                R.id.tvAdd->{
                    if (binding.etAddRules.text.toString().isNullOrEmpty()){
                        showToast("Please enter rule to add")
                        return@observe
                    }
                    addRulesAdapter.list.add(binding.etAddRules.text.toString())
                    addRulesAdapter.notifyDataSetChanged()
                    binding.etAddRules.text?.clear()
                }
                R.id.tvAdd2->{
                    if (binding.etAddHotspots.text.toString().isNullOrEmpty()){
                        showToast("Please enter hotspot to add")
                        return@observe
                    }
                    addHotspotAdapter.list.add(binding.etAddHotspots.text.toString())
                    addHotspotAdapter.notifyDataSetChanged()
                    binding.etAddHotspots.text?.clear()
                }

                R.id.consButton->{
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun initObserver() {

    }

    private fun initAdapter(){
        addSpeciesAdapter = SimpleRecyclerViewAdapter(R.layout.item_report,BR.bean){v,m,pos ->
            when(v.id){
                R.id.ivTick->{
                    val newState = !m.isSelected!!
                    m.isSelected = newState
                    addSpeciesAdapter.notifyItemChanged(pos)
                    if (newState) {
                        if (!selectedSpeciesList.contains(m.title)) {
                            selectedSpeciesList.add(m.title!!)
                        }
                    } else {
                        selectedSpeciesList.remove(m.title)
                    }
                }
            }
        }
        addSpeciesAdapter.list=getSpeciesList()
        binding.rvSpecies.adapter=addSpeciesAdapter


        addRulesAdapter= SimpleRecyclerViewAdapter(R.layout.item_add_rules, BR.bean){v,m,pos->
            when(v.id){
                R.id.tvRemove->{
                    addRulesAdapter.list.removeAt(pos)
                    addRulesAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.rvAddRules.adapter =addRulesAdapter

        addHotspotAdapter= SimpleRecyclerViewAdapter(R.layout.item_add_rules, BR.bean){v,m,pos->
            when(v.id){
                R.id.tvRemove->{
                    addHotspotAdapter.list.removeAt(pos)
                    addHotspotAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.rvAddHotspots.adapter =addHotspotAdapter
    }

    private fun getSpeciesList(): ArrayList<ReplyModel>{
        val list = ArrayList<ReplyModel>()
        list.add(ReplyModel("Pike"))
        list.add(ReplyModel("Pike"))
        list.add(ReplyModel("Perch"))
        list.add(ReplyModel("Perch"))
        list.add(ReplyModel("Char"))
        list.add(ReplyModel("Char"))
        list.add(ReplyModel("Zander"))
        list.add(ReplyModel("Zander"))
        return list
    }
}