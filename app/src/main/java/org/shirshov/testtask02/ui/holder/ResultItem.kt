package org.shirshov.testtask02.ui.holder

import org.shirshov.testtask02.data.Result

class ResultItem private constructor(val type: Type, val data: Result?, val headerDate: String?) {
    val homeWin = data?.homeTeamScore ?: 0 > data?.awayTeamScore ?: 0
    val awayWin = data?.homeTeamScore ?: 0 < data?.awayTeamScore ?: 0

    companion object {

        fun fromData(data: Result) = ResultItem(Type.DATA, data, null)

        fun fromHeaderDate(headerDate: String) = ResultItem(Type.HEADER_DATE, null, headerDate)
    }

    enum class Type {
        HEADER_DATE,
        DATA
    }
}