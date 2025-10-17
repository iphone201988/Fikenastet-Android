package com.fisken_astet.fikenastet.ui.report

import android.util.Log
import com.fisken_astet.fikenastet.base.BaseViewModel
import com.fisken_astet.fikenastet.base.utils.Resource
import com.fisken_astet.fikenastet.base.utils.event.SingleRequestEvent
import com.fisken_astet.fikenastet.data.api.ApiHelper
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportVM @Inject constructor(val apiHelper: ApiHelper): BaseViewModel() {
    val reportObserver = SingleRequestEvent<JsonObject>()

    /** reportApi **/
    fun reportApi(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            reportObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        Log.e("report_api", "reportApi: ${it.body()}", )
                        reportObserver.postValue(Resource.success("REPORT", it.body()))
                    } else
                        reportObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                reportObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }
}