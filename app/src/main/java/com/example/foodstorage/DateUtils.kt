package com.example.foodstorage

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun parseDateToMillis(dateString: String): Long {
        return try {
            val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val date: Date? = format.parse(dateString)
            date?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    fun formatMillisToDate(millis: Long): String {
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return format.format(Date(millis))
    }
}