package org.shirshov.testtask02.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

object Time {

    private val UTC_ZONE_ID = ZoneId.of("UTC")
    private val CURRENT_ZONE_ID get() = ZoneId.systemDefault()

    fun nowUtc() = LocalDateTime.now(UTC_ZONE_ID)

    fun convertUtcToRegionalTime(localDateTime: LocalDateTime): LocalDateTime {
        return convertToAnotherTimezoneFromUtc(localDateTime, CURRENT_ZONE_ID)
    }

    private fun convertToAnotherTimezoneFromUtc(localDateTime: LocalDateTime, zoneId: ZoneId): LocalDateTime {
        return localDateTime.atZone(UTC_ZONE_ID).withZoneSameInstant(zoneId).toLocalDateTime()
    }

}