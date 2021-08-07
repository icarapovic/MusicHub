package dev.chapz.musichub.repository

data class Song(
    val id: Long,
    val title: String,
    val trackNumber: Int,
    val year: Int,
    val duration: Long,
    val dateModified: Long,
    val albumId: Long,
    val albumName: String,
    val artistId: Long,
    val artistName: String,
    val composer: String?,
)
