package org.shirshov.testtask.data

import org.threeten.bp.LocalDateTime

data class Album(
    val albumName: String,
    val artistName: String,
    val id: Long,
    val smallCover: String,
    val cover: String,
    val trackCount: Long,
    val releaseDate: LocalDateTime,
    val genre: String
)