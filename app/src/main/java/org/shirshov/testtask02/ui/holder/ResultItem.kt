package org.shirshov.testtask02.ui.holder

import org.shirshov.testtask02.data.Result

class ResultItem(val data: Result) {
    val homeWin = data.homeTeamScore > data.awayTeamScore
    val awayWin = data.homeTeamScore < data.awayTeamScore
}