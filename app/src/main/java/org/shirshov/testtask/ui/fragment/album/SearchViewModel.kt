package org.shirshov.testtask.ui.fragment.album

import org.shirshov.testtask.network.repository.AlbumsRepository
import org.shirshov.testtask.ui.fragment.BaseViewModel
import org.shirshov.testtask.ui.holder.AlbumItem
import org.shirshov.testtask.util.extension.mutableLiveData

class SearchViewModel(private val repository: AlbumsRepository) : BaseViewModel() {

    val emptyState = mutableLiveData(true)
    val albums = mutableLiveData(listOf<AlbumItem>())
    var lastSearch = ""
        private set

    override fun loadData() {
        search(lastSearch)
    }

    fun search(text: String) {
        if (text == lastSearch) return
        cancel()
        lastSearch = text
        if (text.isBlank()) {
            albums.postValue(mutableListOf())
            emptyState.postValue(true)
        } else {
            load {
                repository.searchForAlbums(lastSearch).onDone {
                    albums.postValue(it.map { album -> AlbumItem(album) }
                        .sortedBy { albumItem -> albumItem.data.albumName }
                        .also { newData -> emptyState.postValue(newData.isNullOrEmpty()) })
                }
            }
        }
    }
}
