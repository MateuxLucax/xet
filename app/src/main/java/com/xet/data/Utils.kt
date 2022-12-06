package com.xet.data

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class Utils {
    companion object {
        fun parseDate(date: String, format: String? = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): LocalDateTime {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format))
        }

        fun formatDate(date: LocalDateTime): String {
            val instant = date.atZone(ZoneId.systemDefault()).toInstant()
            val zoneId = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
            val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

            return zoneId.format(dateFormatter)
        }
    }
}