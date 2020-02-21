package com.parasde.imagepicker.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormat {
    fun format(s: Long): String {
        val sFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

        return sFormat.format(Date(s))
    }
}