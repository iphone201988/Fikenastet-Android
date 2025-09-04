package com.example.fikenastet.ui.dashboard.marketplace

import com.example.fikenastet.base.BaseViewModel
import com.example.fikenastet.base.utils.Resource
import com.example.fikenastet.base.utils.event.SingleRequestEvent
import com.example.fikenastet.data.api.ApiHelper
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketPlaceVM @Inject constructor(
    private val apiHelper: ApiHelper,
) : BaseViewModel() {
    val observeCommon = SingleRequestEvent<JsonObject>()

}

