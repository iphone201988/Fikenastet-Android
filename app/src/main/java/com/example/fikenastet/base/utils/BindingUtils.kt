package com.example.fikenastet.base.utils

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.fikenastet.R
import com.google.android.material.imageview.ShapeableImageView

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
}
