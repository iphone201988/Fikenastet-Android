package com.fisken_astet.fikenastet.base.location

import android.location.Location

interface LocationResultListener {
    fun getLocation(location: Location)
}