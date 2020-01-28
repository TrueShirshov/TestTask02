package org.shirshov.testtask.network.repository

import io.ktor.client.request.parameter
import org.shirshov.testtask.data.Album
import org.shirshov.testtask.data.Song
import org.shirshov.testtask.network.http.HttpService
import org.shirshov.testtask.network.utils.Path
import org.threeten.bp.LocalDateTime

class AlbumsRepository(private val httpService: HttpService) : BaseHttpRepository() {

    suspend fun searchForAlbums(query: String) = request {
        httpService.get<ResponseContainer>(Path.Http.SEARCH) {
            parameter("term", query)
            parameter("attribute", "albumTerm")
            parameter("limit", "30")
            parameter("media", "music")
            parameter("entity", "album")
        }?.results?.map { it.toAlbum() }
    }

    suspend fun songListByAlbum(albumId: Long) = request {
        httpService.get<ResponseContainer>(Path.Http.LOOKUP) {
            parameter("id", albumId)
            parameter("media", "music")
            parameter("entity", "song")
        }?.results?.filterIndexed { index, _ -> index > 0 }?.map { it.toSong() }
    }

    private data class ResponseContainer(val resultCount: Long, val results: List<ITunesWrapper>?)

    private data class ITunesWrapper(
        val collectionId: Long,
        val artistName: String,
        val collectionName: String,
        val artworkUrl60: String?,
        val artworkUrl100: String?,
        val trackCount: Long,
        val releaseDate: LocalDateTime,
        val primaryGenreName: String,
        val trackName: String?,
        val discNumber: Long?,
        val trackNumber: Long?,
        val trackTimeMillis: Long?
    ) {
        fun toAlbum() = Album(collectionName, artistName, collectionId, artworkUrl60 ?: "", artworkUrl100 ?: "", trackCount ?: 0, releaseDate, primaryGenreName)

        fun toSong() = Song(trackName ?: "-", collectionName, trackNumber ?: 0, discNumber ?: 0, trackTimeMillis ?: 0)
    }
}