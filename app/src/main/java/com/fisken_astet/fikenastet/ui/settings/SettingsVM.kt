package com.fisken_astet.fikenastet.ui.settings

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(val apiHelper: ApiHelper) : BaseViewModel(){
    val settingsObserver = SingleRequestEvent<JsonObject>()

    /** update profile api **/
    fun updateProfileApi(url: String,request: HashMap<String, RequestBody>,part: MultipartBody.Part?) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiForPostMultipart(url,request,part).let {
                    if (it.isSuccessful) {
                        settingsObserver.postValue(Resource.success("UPDATE_PROFILE", it.body()))
                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** notification toggle **/
    fun notificationToggleApi(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        settingsObserver.postValue(Resource.success("NOTIFICATION_TOGGLE", it.body()))
                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }


    /** account integrity toggle **/
    fun accountIntegrityToggle(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        settingsObserver.postValue(Resource.success("ACCOUNT_TOGGLE", it.body()))
                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** logout api **/
    fun logoutApi(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        settingsObserver.postValue(Resource.success("LOGOUT", it.body()))
                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }


    /** export data  api **/
    fun exportDataApi(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiGetOnlyAuthToken(url).let {
                    if (it.isSuccessful) {
                        settingsObserver.postValue(Resource.success("EXPORT_DATA", it.body()))
                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** two fa apis **/
    fun twoFactorAuthApi(url: String,request: HashMap<String, Any>,type:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        if (type==1){
                            settingsObserver.postValue(Resource.success("TWO_FA_VERIFY", it.body()))
                        }
                        else if (type==2){
                            settingsObserver.postValue(Resource.success("TWO_FA_VERIFY_VIA_LOGIN", it.body()))
                        }
                        else{
                            settingsObserver.postValue(Resource.success("TWO_FA_ENABLE", it.body()))
                        }

                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** handle restrict / block user toggle  **/
    fun getBlockedUserList(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                            settingsObserver.postValue(Resource.success("BLOCKED_USERS", it.body()))

                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** unblock User **/
    fun unblockUserApi(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsObserver.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                            settingsObserver.postValue(Resource.success("UNBLOCK_USER", it.body()))

                    } else
                        settingsObserver.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                settingsObserver.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }
}