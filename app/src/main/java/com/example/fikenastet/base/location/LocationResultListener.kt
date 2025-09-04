package com.example.fikenastet.base.location

import android.location.Location

interface LocationResultListener {
    fun getLocation(location: Location)
}