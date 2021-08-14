package dev.chapz.musichub.repository

import android.support.v4.media.MediaMetadataCompat

interface SongRepository {
    fun getAllSongMetadata(): MutableList<MediaMetadataCompat>
}