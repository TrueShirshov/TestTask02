package org.shirshov.testtask02.ui.fragment.sportevent

import org.shirshov.testtask02.ui.fragment.BaseViewModel
import org.shirshov.testtask02.ui.holder.FilterItem
import org.shirshov.testtask02.util.extension.mutableLiveData
import org.shirshov.testtask02.util.extension.postInverse

class FilterViewModel(private val sharedModel: SportSharedViewModel) : BaseViewModel() {

    val filter = mutableLiveData(listOf<FilterItem>())

    override fun loadData() {
        filter.postValue(sharedModel.competitions.values.sortedBy { it.competitionName }.map { FilterItem(it) })
    }

    fun change(index: Int) {
        filter.value!![index].data.active = filter.value!![index].checked.postInverse()
    }

    fun setAll(selected: Boolean) {
        filter.value!!.forEach {
            it.data.active = selected
            it.checked.postValue(selected)
        }
    }

}
