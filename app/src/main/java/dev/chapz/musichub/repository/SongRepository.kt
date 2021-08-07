package dev.chapz.musichub.repository

interface SongRepository {
    fun getAllSongs(): List<Song>
}