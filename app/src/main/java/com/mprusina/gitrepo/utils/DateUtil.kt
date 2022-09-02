package com.mprusina.gitrepo.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: String?): String {
    if (date.isNullOrEmpty()) {
        return "Date format error"
    }
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val currentDate = formatter.parse(date) as Date
    val newFormatter = SimpleDateFormat("dd/MM/yyyy H:mm:ss", Locale.getDefault())
    return newFormatter.format(currentDate)
}