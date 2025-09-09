package com.example.fikenastet.base.utils

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.fikenastet.R
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object BindingUtils {

    @BindingAdapter("setImageFromUrl")
    @JvmStatic
    fun setImageFromUrl(image: ShapeableImageView, url: String?) {
        if (url != null) {
            Glide.with(image.context).load(url).placeholder(R.drawable.user).error(R.drawable.user)
                .into(image)
        }
    }

    @BindingAdapter("setImageFromDrawable")
    @JvmStatic
    fun setImageFromDrawable(imageview: ImageView, image: Int?) {
        if (image != null) {
            imageview.setImageResource(image)

        }
    }

    @BindingAdapter("removeLine")
    @JvmStatic
    fun removeLine(imageview: View, pos: Int?) {
        if (pos != null) {
            if (pos == 3) {
                imageview.visibility = View.GONE
            } else {
                imageview.visibility = View.VISIBLE
            }
        }
    }

    @BindingAdapter("setBackground")
    @JvmStatic
    fun setBackground(imageview: AppCompatTextView, pos: Boolean) {
        if (pos) {
            val color = ContextCompat.getColor(imageview.context, R.color.app_text)
            ViewCompat.setBackgroundTintList(imageview, ColorStateList.valueOf(color))
            imageview.setTextColor(ContextCompat.getColor(imageview.context, R.color.white))
        } else {
            val color = ContextCompat.getColor(imageview.context, R.color.transparent)
            ViewCompat.setBackgroundTintList(imageview, null)
            imageview.setTextColor(ContextCompat.getColor(imageview.context, R.color.colorPrimary))
        }
    }


    fun styleSystemBars(activity: Activity, color: Int) {
        activity.window.navigationBarColor = color
    }

    fun statusBarStyleWhite(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun statusBarStyleBlack(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Ensures black text/icons
            activity.window.statusBarColor = Color.TRANSPARENT // Transparent status bar
        }
    }

    @BindingAdapter("pos_reply", "size_reply")
    @JvmStatic
    fun setViewLineVisibility(view: View, pos: Int?, size: Int?) {
        if (pos != null && size != null) {
            view.visibility = if (pos == size - 1) View.INVISIBLE else View.VISIBLE
        } else {
            view.visibility = View.VISIBLE
        }
    }


        @BindingAdapter("pos_reply2", "size_reply2")
        @JvmStatic
        fun setSettingsUi(view: AppCompatTextView, pos: Int?, size: Int?) {
            if (pos != null && size != null) {
                when (pos) {
                    size - 1 -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.red))
                        view.setBackgroundDrawable(ContextCompat.getDrawable(view.context,R.drawable.bg_radius_stroke_red))
                        view.backgroundTintList = null
                    }
                    size - 2 -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                        view.backgroundTintList =
                            ContextCompat.getColorStateList(view.context, R.color.red)
                    }
                    else -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
                        view.backgroundTintList = null
                    }
                }
            } else {
                view.setTextColor(ContextCompat.getColor(view.context,R.color.colorPrimary))
                view.backgroundTintList = null
            }
        }

    /** handle password visibility **/
    private var isPasswordVisible: Boolean = false
    /*** show or hide password ***/
    fun showOrHidePassword(editText: AppCompatEditText, imageView: AppCompatImageView) {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            // Show the password
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imageView.setImageResource(R.drawable.solar_eye)
        } else {
            // Hide the password
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            imageView.setImageResource(R.drawable.solar_eye)
        }
        editText.setSelection(editText.text!!.length)
    }

    /** format date on basis of today & yesterday **/
    fun formatDate(isoDate: String): Pair<String, String> {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")  // Parse in UTC

        val dateFormat = SimpleDateFormat("EEE, d MMM, yyyy", Locale.getDefault())  // Date format
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())  // Time format

        try {
            val date = isoFormat.parse(isoDate)

            if (date != null) {
                val currentDate = Calendar.getInstance()
                val calendar = Calendar.getInstance()
                calendar.time = date
                if (isToday(calendar, currentDate)) {
                    return Pair("Today", timeFormat.format(date))
                }

                // Check if the date is yesterday
                if (isYesterday(calendar)) {
                    return Pair("Yesterday", timeFormat.format(date))
                }

                // If it's neither today nor yesterday, return the formatted date
                return Pair(dateFormat.format(date), timeFormat.format(date))
            } else {
                return Pair("", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Pair("", "")
        }
    }

    private fun isToday(messageDate: Calendar, currentDate: Calendar): Boolean {
        return messageDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) && messageDate.get(
            Calendar.DAY_OF_YEAR
        ) == currentDate.get(Calendar.DAY_OF_YEAR)
    }

    // Check if the given date is yesterday
    private fun isYesterday(messageDate: Calendar): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        return messageDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && messageDate.get(
            Calendar.DAY_OF_YEAR
        ) == yesterday.get(Calendar.DAY_OF_YEAR)
    }
}
