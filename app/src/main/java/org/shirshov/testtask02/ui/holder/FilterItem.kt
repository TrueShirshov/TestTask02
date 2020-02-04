package org.shirshov.testtask02.ui.holder

import org.shirshov.testtask02.data.FilterRow
import org.shirshov.testtask02.util.extension.mutableLiveData

class FilterItem(val data: FilterRow) {
    val checked = mutableLiveData(data.active)
}