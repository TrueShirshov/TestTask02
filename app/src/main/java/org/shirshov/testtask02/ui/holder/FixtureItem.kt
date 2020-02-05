package org.shirshov.testtask02.ui.holder

import org.shirshov.testtask02.data.Fixture

class FixtureItem private constructor(val type: Type, val data: Fixture?, val headerDate: String?) {

    companion object {

        fun fromData(data: Fixture) = FixtureItem(Type.DATA, data, null)

        fun fromHeaderDate(headerDate: String) = FixtureItem(Type.HEADER_DATE, null, headerDate)
    }

    enum class Type {
        HEADER_DATE,
        DATA
    }
}