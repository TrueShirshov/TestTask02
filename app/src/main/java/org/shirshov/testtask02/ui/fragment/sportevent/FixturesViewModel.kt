package org.shirshov.testtask02.ui.fragment.sportevent

import io.reactivex.disposables.Disposable
import org.shirshov.testtask02.data.FilterRow
import org.shirshov.testtask02.network.repository.SportEventRepository
import org.shirshov.testtask02.ui.fragment.BaseViewModel
import org.shirshov.testtask02.ui.holder.FixtureItem
import org.shirshov.testtask02.util.extension.mutableLiveData

class FixturesViewModel(private val repository: SportEventRepository, private val sharedModel: SportSharedViewModel) : BaseViewModel() {

    val fixtures = mutableLiveData(listOf<FixtureItem>())
    private val updateTriggerDisposable: Disposable = sharedModel.updateTrigger.subscribe { reload() }

    override fun loadData() {
        load {
            repository.fixtures().onDone {
                sharedModel.processNewCompetitions(it.map { fixture -> FilterRow(fixture.competitionId, fixture.competitionName, true) })
                fixtures.postValue(it.filter { fixture -> sharedModel.isActive(fixture.competitionId) }.map { fixture -> FixtureItem(fixture) })
            }
        }
    }

    override fun onCleared() {
        updateTriggerDisposable.dispose()
        super.onCleared()
    }
}
