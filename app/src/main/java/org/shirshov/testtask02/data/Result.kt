package org.shirshov.testtask02.data

import org.threeten.bp.LocalDateTime

data class Result(
    val competitionId: Long,
    val competitionName: String,
    val venue: String,
    val date: LocalDateTime,
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamScore:Int,
    val awayTeamScore:Int
)