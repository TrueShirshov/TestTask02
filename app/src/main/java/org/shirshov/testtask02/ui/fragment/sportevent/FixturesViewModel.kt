package org.shirshov.testtask02.ui.fragment.sportevent

import io.reactivex.disposables.Disposable
import org.shirshov.testtask02.network.repository.SportEventRepository
import org.shirshov.testtask02.ui.fragment.BaseViewModel
import org.shirshov.testtask02.ui.holder.FixtureItem
import org.shirshov.testtask02.ui.util.Format
import org.shirshov.testtask02.util.extension.mutableLiveData
import org.threeten.bp.LocalDateTime

class FixturesViewModel(private val repository: SportEventRepository, private val sharedModel: SportSharedViewModel) : BaseViewModel() {

    val fixtures = mutableLiveData(listOf<FixtureItem>())
    val emptyState = mutableLiveData(false)
    private val updateTriggerDisposable: Disposable = sharedModel.updateTrigger.subscribe { reload() }

    override fun loadData() {
        load {
            repository.fixtures().onDone {
                sharedModel.processNewCompetitions(it.map { fixture -> Pair(fixture.competitionId, fixture.competitionName) })
                val filteredData = addHeaders(it.filter { fixture -> sharedModel.isActive(fixture.competitionId) }.map { fixture -> FixtureItem.fromData(fixture) })
                fixtures.postValue(filteredData)
                emptyState.postValue(filteredData.isNullOrEmpty())
            }
        }
    }

    private fun addHeaders(filteredData: List<FixtureItem>): List<FixtureItem> {
        val result = mutableListOf<FixtureItem>()
        addHeadersHelper(filteredData, result, { it.data?.date }, { FixtureItem.fromHeaderDate(it) })
        return result
    }

    override fun onCleared() {
        updateTriggerDisposable.dispose()
        super.onCleared()
    }
}

fun <T> addHeadersHelper(filteredData: List<T>, resultItems: MutableList<T>, getDate: (T) -> LocalDateTime?, makeHeader: (String) -> T): Unit {
    val uniqueHeaders = mutableListOf<String>()
    for (item in filteredData) {
        val date = Format.dateAsMonth(getDate(item))
        if (!uniqueHeaders.contains(date)) {
            uniqueHeaders.add(date)
            resultItems.add(makeHeader(date))
        }
        resultItems.add(item)
    }
}