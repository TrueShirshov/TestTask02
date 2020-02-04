package org.shirshov.testtask02.ui.fragment.sportevent

import org.shirshov.testtask02.ui.fragment.BaseViewModel
import org.shirshov.testtask02.ui.holder.FilterItem
import org.shirshov.testtask02.util.extension.mutableLiveData

class FilterViewModel(private val sharedModel: SportSharedViewModel) : BaseViewModel() {

    val filter = mutableLiveData(listOf<FilterItem>())

    override fun loadData() {
        filter.postValue(sharedModel.competitions.values.sortedBy { it.competitionName }.map { FilterItem(it) })
    }

}
