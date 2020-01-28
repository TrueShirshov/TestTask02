package org.shirshov.testtask.ui.util

import org.shirshov.testtask.R
import org.shirshov.testtask.util.extension.toStringRes
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object Format {

    @JvmStatic
    fun duration(millis: Long) = String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) % 60)

    @JvmStatic
    fun disc(value: Number) = R.string.format_disc.toStringRes(value)

    @JvmStatic
    fun track(value: Number) = R.string.format_track.toStringRes(value)

    @JvmStatic
    fun dateAsYear(date: LocalDateTime): String = date.format(DateTimeFormatter.ofPattern("YYYY"))
}