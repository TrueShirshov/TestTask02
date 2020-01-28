package org.shirshov.testtask.util

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.shirshov.testtask.network.http.HttpService
import org.shirshov.testtask.network.repository.AlbumsRepository
import org.shirshov.testtask.ui.fragment.album.SearchDetailsViewModel
import org.shirshov.testtask.ui.fragment.album.SearchViewModel

/**
 * KoIn dependency injection declarations
 */
val testTask01Module = module {
    viewModel { SearchViewModel(get()) }
    viewModel { SearchDetailsViewModel(get()) }

    single { AlbumsRepository(get()) }
    single { HttpService() }
}