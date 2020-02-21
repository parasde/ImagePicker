package com.parasde.imagepicker.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class CheckPermission(private val mContext: Context) {
    fun check(): Boolean {
        return ContextCompat.checkSelfPermission(
            mContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}