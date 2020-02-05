package org.shirshov.testtask02.ui.fragment.sportevent

import io.reactivex.disposables.Disposable
import org.shirshov.testtask02.network.repository.SportEventRepository
import org.shirshov.testtask02.ui.fragment.BaseViewModel
import org.shirshov.testtask02.ui.holder.ResultItem
import org.shirshov.testtask02.ui.util.Format
import org.shirshov.testtask02.util.extension.mutableLiveData

class ResultsViewModel(private val repository: SportEventRepository, private val sharedModel: SportSharedViewModel) : BaseViewModel() {

    val results = mutableLiveData(listOf<ResultItem>())
    val emptyState = mutableLiveData(false)
    private val updateTriggerDisposable: Disposable = sharedModel.updateTrigger.subscribe { reload() }

    override fun loadData() {
        load {
            repository.results().onDone {
                sharedModel.processNewCompetitions(it.map { fixture -> Pair(fixture.competitionId, fixture.competitionName) })
                val filteredData = addHeaders(it.filter { result -> sharedModel.isActive(result.competitionId) }.map { result -> ResultItem.fromData(result) })
                results.postValue(filteredData)
                emptyState.postValue(filteredData.isNullOrEmpty())
            }
        }
    }

    private fun addHeaders(filteredData: List<ResultItem>): List<ResultItem> {
        val result = mutableListOf<ResultItem>()
        val uniqueHeaders = mutableListOf<String>()
        for (item in filteredData) {
            val date = Format.dateAsMonth(item.data?.date)
            if (!uniqueHeaders.contains(date)) {
                uniqueHeaders.add(date)
                result.add(ResultItem.fromHeaderDate(date))
            }
            result.add(item)
        }
        return result
    }

    override fun onCleared() {
        updateTriggerDisposable.dispose()
        super.onCleared()
    }
}
