package org.shirshov.testtask.ui.fragment.album

import org.shirshov.testtask.network.repository.AlbumsRepository
import org.shirshov.testtask.ui.fragment.BaseViewModel

class SearchViewModel : BaseViewModel() {

    private val repo = AlbumsRepository()

    override fun loadData() {
        load {
            repo.test().onDone { }
        }
    }
}
