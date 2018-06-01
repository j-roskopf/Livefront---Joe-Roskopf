package com.livefront.android.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance

private const val DATE_FORMAT_FOR_API = "yyyy-MM-dd"
private const val DATE_FORMAT_YEAR_FOR_API = "yyyy-MM-dd"

fun Calendar.formattedCurrentDate(): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_FOR_API, Locale.US)
    return dateFormat.format(getInstance().time)
}

fun Calendar.getCurrentYear(): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_YEAR_FOR_API, Locale.US)
    return dateFormat.format(getInstance().time)
}

