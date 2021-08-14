package dev.chapz.musichub.repository

import android.support.v4.media.MediaMetadataCompat

interface AlbumRepository {
    fun getAllAlbumsMetadata(): MutableList<MediaMetadataCompat>
}