package org.shirshov.testtask.util

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.shirshov.testtask.ui.fragment.album.SearchViewModel

/**
 * KoIn dependency injection declarations
 */
val testTask01Module = module {
    viewModel { SearchViewModel() }

//    single { UserSocketService() }
//    single { MarketDataSocketService() }
//    single { UserHttpService() }
}