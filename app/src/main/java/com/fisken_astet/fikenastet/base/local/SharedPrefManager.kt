package com.fisken_astet.fikenastet.base.local

import android.content.SharedPreferences
import com.fisken_astet.fikenastet.data.model.User
import com.google.gson.Gson
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    object KEY {
        const val IS_USER_DATA = "is_user_data"
        const val IS_BEARER = "is_bearer"
        const val IS_LOGGED_IN = "is_logged_in"
    }

    fun setLoginData(isFirst: User) {
        val gson = Gson()
        val json = gson.toJson(isFirst)
        val editor = sharedPreferences.edit()
        editor.putString(KEY.IS_USER_DATA, json)
        editor.apply()
    }
    fun getLoginData(): User? {
        val json = sharedPreferences.getString(KEY.IS_USER_DATA, null)
        return if (!json.isNullOrEmpty()) {
            try {
                Gson().fromJson(json, User::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    fun setToken(isFirst: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.IS_BEARER, isFirst)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY.IS_BEARER, "")
    }

    fun setLoggedIn(isFirst: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY.IS_LOGGED_IN, isFirst)
        editor.apply()
    }

    fun getIsLoggedIn(): Boolean? {
        return sharedPreferences.getBoolean(KEY.IS_LOGGED_IN, false)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}