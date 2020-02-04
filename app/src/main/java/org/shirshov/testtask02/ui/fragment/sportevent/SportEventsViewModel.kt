package org.shirshov.testtask02.ui.fragment.sportevent

import org.shirshov.testtask02.ui.fragment.BaseViewModel

class SportEventsViewModel(private val sharedModel: SportSharedViewModel) : BaseViewModel() {

    override fun loadData() {}

    fun onFilterEditDone() = sharedModel.updateTrigger.onNext(Unit)
}
