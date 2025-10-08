package com.fisken_astet.fikenastet.ui.common_classes.follower_following

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
class FollowerFollowingVM @Inject constructor(val apiHelper: ApiHelper): BaseViewModel() {
    val followObserver= SingleRequestEvent<JsonObject>()

    /** follow actions api **/
    fun updateFollowActionApi(url: String,request: HashMap<String, Any>,type: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            followObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        if (type==3){
                            followObserver.postValue(Resource.success("FOLLOW_UNFOLLOW", it.body()))
                        }
                        else if (type==2){
                            followObserver.postValue(Resource.success("FOLLOWING", it.body()))
                        }
                        else{
                            followObserver.postValue(Resource.success("FOLLOWER", it.body()))
                        }
                    } else
                        followObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                followObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }
    
}