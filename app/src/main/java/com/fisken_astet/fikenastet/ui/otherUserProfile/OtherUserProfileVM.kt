package com.fisken_astet.fikenastet.ui.otherUserProfile

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
class OtherUserProfileVM @Inject constructor(val apiHelper: ApiHelper) : BaseViewModel() {
    val otherProfileObserver = SingleRequestEvent<JsonObject>()
    
    /** get profile data **/
    fun getProfileData(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            otherProfileObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiGetOnlyAuthToken(url).let {
                    if (it.isSuccessful) {
                        otherProfileObserver.postValue(Resource.success("PROFILE_DATA", it.body()))
                    } else
                        otherProfileObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                otherProfileObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** follow actions api **/
    fun updateFollowActionApi(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            otherProfileObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        otherProfileObserver.postValue(Resource.success("FOLLOW_UNFOLLOW", it.body()))
                    } else
                        otherProfileObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                otherProfileObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }


    /** handle restrict / block user toggle  **/
    fun handleRestrictBlockToggle(url: String,request: HashMap<String, Any>,type:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            otherProfileObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        Log.e("response", "handleRestrictBlockToggle: ${it.body()}", )
                        if (type==1){
                            otherProfileObserver.postValue(Resource.success("BLOCK", it.body()))
                        }
                        else if (type==2){
                            otherProfileObserver.postValue(Resource.success("UNBLOCK", it.body()))
                        }
                        else if (type==3){
                            otherProfileObserver.postValue(Resource.success("RESTRICT", it.body()))
                        }
                        else{
                            otherProfileObserver.postValue(Resource.success("UNRESTRICT", it.body()))
                        }

                    } else
                        otherProfileObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                otherProfileObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }
}