package org.shirshov.testtask02.ui.util

import org.shirshov.testtask02.R
import org.shirshov.testtask02.util.Time
import org.shirshov.testtask02.util.extension.toStringRes
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

object Format {

    private val locale get() = Locale.getDefault()

    @JvmStatic
    fun competition(value: String?) = value?.toUpperCase(locale)

    @JvmStatic
    fun venue(venue: String?) = R.string.format_venue.toStringRes(venue ?: "")

    @JvmStatic
    fun dateAsMonth(date: LocalDateTime?) = date?.formatAsRegionalTime("MMMM YYYY") ?: ""

    @JvmStatic
    fun date(date: LocalDateTime?) = date?.formatAsRegionalTime(R.string.pattern_standard.toStringRes()) ?: ""

    @JvmStatic
    fun monthDay(date: LocalDateTime?) = date?.formatAsRegionalTime("dd")

    @JvmStatic
    fun weekDay(date: LocalDateTime?) = date?.formatAsRegionalTime("EEE")?.toUpperCase(locale)

    private fun LocalDateTime.formatAsRegionalTime(pattern: String) = Time.convertUtcToRegionalTime(this).format(DateTimeFormatter.ofPattern(pattern, locale))

}