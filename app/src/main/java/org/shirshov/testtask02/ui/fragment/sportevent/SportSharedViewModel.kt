package org.shirshov.testtask02.ui.fragment.sportevent

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject
import org.shirshov.testtask02.data.FilterRow

class SportSharedViewModel() : ViewModel() {

    val competitions = hashMapOf<Long, FilterRow>()
    val updateTrigger = PublishSubject.create<Unit>()

    fun processNewCompetitions(newCompetitions: List<FilterRow>) {
        newCompetitions.forEach { newCompetition ->
            if (!competitions.keys.contains(newCompetition.competitionId)) {
                competitions[newCompetition.competitionId] = newCompetition
            }
        }
    }

    fun isActive(competitionId: Long) = competitions[competitionId]!!.active

    override fun onCleared() {
        updateTrigger.onComplete()
        super.onCleared()
    }
}