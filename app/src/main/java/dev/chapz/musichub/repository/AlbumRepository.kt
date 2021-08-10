package dev.chapz.musichub.repository

import android.support.v4.media.MediaBrowserCompat.MediaItem

interface AlbumRepository {
    fun getAllAlbums(): MutableList<MediaItem>
}