package org.shirshov.testtask02.ui.fragment.sportevent

import io.reactivex.disposables.Disposable
import org.shirshov.testtask02.data.FilterRow
import org.shirshov.testtask02.network.repository.SportEventRepository
import org.shirshov.testtask02.ui.fragment.BaseViewModel
import org.shirshov.testtask02.ui.holder.ResultItem
import org.shirshov.testtask02.util.extension.mutableLiveData

class ResultsViewModel(private val repository: SportEventRepository, private val sharedModel: SportSharedViewModel) : BaseViewModel() {

    val results = mutableLiveData(listOf<ResultItem>())
    private val updateTriggerDisposable: Disposable = sharedModel.updateTrigger.subscribe { reload() }

    override fun loadData() {
        load {
            repository.results().onDone {
                sharedModel.processNewCompetitions(it.map { fixture -> FilterRow(fixture.competitionId, fixture.competitionName, true) })
                results.postValue(it.filter { result -> sharedModel.isActive(result.competitionId) }.map { result -> ResultItem(result) })
            }
        }
    }

    override fun onCleared() {
        updateTriggerDisposable.dispose()
        super.onCleared()
    }
}
