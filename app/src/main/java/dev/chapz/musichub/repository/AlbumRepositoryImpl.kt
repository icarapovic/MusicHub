package dev.chapz.musichub.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaMetadataCompat
import dev.chapz.musichub.service.*

class AlbumRepositoryImpl(private val mediaStore: ContentResolver) : AlbumRepository {

    private val songProjection = arrayOf(
        Albums._ID,
        Albums.ALBUM,
        Albums.ARTIST,
        Albums.NUMBER_OF_SONGS,
    )

    private val albumArtworkUri = Uri.parse("content://media/external/audio/albumart")

    override fun getAllAlbums(): MutableList<MediaItem> {
        val cursor = queryMediaStoreAlbums()
        return extractAlbumsFromCursor(cursor)
    }

    private fun queryMediaStoreAlbums(selection: String? = null, selectionArgs: Array<String>? = null): Cursor? {
        return mediaStore.query(
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL),
            songProjection,
            selection,
            selectionArgs,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
    }

    private fun extractAlbumsFromCursor(cursor: Cursor?): MutableList<MediaItem> {
        val songs = arrayListOf<MediaItem>()

        // if the cursor is not null
        cursor?.let { c ->
            // and the cursor has at least one entry
            if (c.moveToFirst()) {
                // iterate rows and create song objects
                do {
                    songs.add(getMediaItemFromCursor(c))
                } while (c.moveToNext())
            }
        }

        return songs
    }

    private fun getMediaItemFromCursor(cursor: Cursor): MediaItem {
        val id = cursor.getString(Albums._ID)
        val title = cursor.getString(Albums.ALBUM)
        val trackCount = cursor.getLong(Albums.NUMBER_OF_SONGS)
        val albumArtUri = ContentUris.withAppendedId(albumArtworkUri, id.toLong()).toString()
        val artist = cursor.getString(Albums.ARTIST)

        val metadataBuilder = MediaMetadataCompat.Builder()
        metadataBuilder.id = id
        metadataBuilder.title = title
        metadataBuilder.displayTitle = title
        metadataBuilder.artist = artist
        metadataBuilder.albumArtUri = albumArtUri
        metadataBuilder.trackCount = trackCount
        metadataBuilder.mediaUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toLong()).toString()
        metadataBuilder.flag = FLAG_PLAYABLE

        return MediaItem(metadataBuilder.build().description, FLAG_PLAYABLE)
    }
}