package org.shirshov.testtask.ui.fragment.album

import org.shirshov.testtask.data.Album
import org.shirshov.testtask.network.repository.AlbumsRepository
import org.shirshov.testtask.ui.fragment.BaseViewModel
import org.shirshov.testtask.ui.holder.AlbumItem
import org.shirshov.testtask.ui.holder.SongItem
import org.shirshov.testtask.util.extension.mutableLiveData

class SearchDetailsViewModel(private val repository: AlbumsRepository) : BaseViewModel() {

    lateinit var album: Album
    val songs = mutableLiveData(listOf<SongItem>())

    override fun loadData() {
        load {
            repository.songListByAlbum(album.id).onDone { songs.postValue(it.map { song -> SongItem(song) }/*.sortedBy { albumItem -> albumItem.data.albumName }*/) }
        }
    }
}
