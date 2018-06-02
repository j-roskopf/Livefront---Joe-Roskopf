package com.livefront.android.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance

private const val DATE_FORMAT_YEAR_FOR_API = "yyyy"


/**
 * Extension function to get the current year, formatted with US locale
 *
 * @return current year in yyyy format
 */
fun Calendar.getCurrentYear(): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_YEAR_FOR_API, Locale.US)
    return dateFormat.format(getInstance().time)
}

