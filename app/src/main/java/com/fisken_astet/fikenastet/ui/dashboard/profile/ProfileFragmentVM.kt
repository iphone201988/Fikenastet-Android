package com.fisken_astet.fikenastet.ui.dashboard.profile

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
class ProfileFragmentVM @Inject constructor(val apiHelper: ApiHelper) : BaseViewModel(){
    val profileObserver= SingleRequestEvent<JsonObject>()

    /** get profile data **/
    fun getProfileData(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            profileObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiGetOnlyAuthToken(url).let {
                    if (it.isSuccessful) {
                        profileObserver.postValue(Resource.success("PROFILE_DATA", it.body()))
                    } else
                        profileObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                profileObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }
}