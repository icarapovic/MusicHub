package dev.chapz.musichub.repository

import android.support.v4.media.MediaBrowserCompat.MediaItem

interface SongRepository {
    fun getAllSongs(): MutableList<MediaItem>
}