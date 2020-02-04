package org.shirshov.testtask02.util

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.shirshov.testtask02.network.http.HttpService
import org.shirshov.testtask02.network.repository.SportEventRepository
import org.shirshov.testtask02.ui.fragment.sportevent.*

/**
 * KoIn dependency injection declarations
 */
val testTask02Module = module {

    viewModel { SportSharedViewModel() }
    viewModel { (sharedModel: SportSharedViewModel) -> SportEventsViewModel(sharedModel) }
    viewModel { (sharedModel: SportSharedViewModel) -> FixturesViewModel(get(), sharedModel) }
    viewModel { (sharedModel: SportSharedViewModel) -> ResultsViewModel(get(), sharedModel) }
    viewModel { (sharedModel: SportSharedViewModel) -> FilterViewModel(sharedModel) }

    single { SportEventRepository(get()) }
    single { HttpService() }
}