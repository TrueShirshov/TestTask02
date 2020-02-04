package org.shirshov.testtask02.ui.util

import org.shirshov.testtask02.R
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
    fun date(date: LocalDateTime?) = date?.format(DateTimeFormatter.ofPattern(R.string.pattern_standard.toStringRes())) ?: ""

    @JvmStatic
    fun monthDay(date: LocalDateTime?) = date?.format(DateTimeFormatter.ofPattern("dd", locale))

    @JvmStatic
    fun weekDay(date: LocalDateTime?) = date?.format(DateTimeFormatter.ofPattern("EEE", locale))?.toUpperCase(locale)
}