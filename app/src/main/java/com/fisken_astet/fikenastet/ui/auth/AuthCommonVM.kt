package com.fisken_astet.fikenastet.ui.auth

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
class AuthCommonVM @Inject constructor(
    private val apiHelper: ApiHelper,
) : BaseViewModel() {
    val observeCommon = SingleRequestEvent<JsonObject>()
    /** signup api **/
    fun signupApi(url: String,request: HashMap<String, RequestBody>,part: MultipartBody.Part?) {
        CoroutineScope(Dispatchers.IO).launch {
            observeCommon.postValue(Resource.loading(null))
            try {
                apiHelper.apiForPostMultipartWithoutAuth(url,request,part).let {
                    if (it.isSuccessful) {
                        observeCommon.postValue(Resource.success("SIGNUP", it.body()))
                    } else
                        observeCommon.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                observeCommon.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** complete profile api **/
    fun completeProfileApi(url: String,request: HashMap<String, RequestBody>,part: MultipartBody.Part?) {
        CoroutineScope(Dispatchers.IO).launch {
            observeCommon.postValue(Resource.loading(null))
            try {
                apiHelper.apiForPostMultipart(url,request,part).let {
                    if (it.isSuccessful) {
                        observeCommon.postValue(Resource.success("COMPLETE_PROFILE", it.body()))
                    } else
                        observeCommon.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                observeCommon.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** verify email & resend **/
    fun verifyEmailAndResend(url: String,request: HashMap<String, Any>,type: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            observeCommon.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        if (type==1) {
                            observeCommon.postValue(Resource.success("VERIFY_OTP", it.body()))
                        }
                        else{
                            observeCommon.postValue(Resource.success("RESEND_OTP", it.body()))
                        }
                    } else
                        observeCommon.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                observeCommon.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** verify email & resend for forget **/
    fun verifyEmailAndResendForForget(url: String,request: HashMap<String, Any>,type: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            observeCommon.postValue(Resource.loading(null))
            try {
                apiHelper.apiForRawBody(request,url).let {
                    if (it.isSuccessful) {
                        if (type==2) {
                            observeCommon.postValue(Resource.success("VERIFY_OTP_FORGET", it.body()))
                        }
                        else if (type==3){
                            observeCommon.postValue(Resource.success("NEW_PASSWORD", it.body()))
                        }
                        else{
                            observeCommon.postValue(Resource.success("RESEND_OTP_FORGET", it.body()))
                        }
                    } else
                        observeCommon.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                observeCommon.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }

    /** login api **/
    fun loginApi(url: String,request: HashMap<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            observeCommon.postValue(Resource.loading(null))
            try {
                apiHelper.apiPostForRawBody(url,request).let {
                    if (it.isSuccessful) {
                        observeCommon.postValue(Resource.success("LOGIN", it.body()))
                    } else
                        observeCommon.postValue(  Resource.error(handleErrorResponse(it.errorBody(), it.code()), null))
                }
            } catch (e: Exception) {
                observeCommon.postValue(
                    Resource.error(
                        e.message, null
                    )
                )
            }

        }
    }
}

