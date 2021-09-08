package dev.chapz.musichub.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaMetadataCompat
import dev.chapz.musichub.service.*

class SongRepositoryImpl(private val mediaStore: ContentResolver) : SongRepository {

    private val songProjection = arrayOf(
        BaseColumns._ID, // 0
        MediaStore.Audio.AudioColumns.TITLE, // 1
        MediaStore.Audio.AudioColumns.TRACK, // 2
        MediaStore.Audio.AudioColumns.YEAR, // 3
        MediaStore.Audio.AudioColumns.DURATION, // 4
        MediaStore.Audio.AudioColumns.DATE_MODIFIED, // 6
        MediaStore.Audio.AudioColumns.ALBUM_ID, // 7
        MediaStore.Audio.AudioColumns.ALBUM, // 8
        MediaStore.Audio.AudioColumns.ARTIST_ID, // 9
        MediaStore.Audio.AudioColumns.ARTIST, // 10
        MediaStore.Audio.AudioColumns.COMPOSER, // 11
    )

    private val songIdSelection = MediaStore.Audio.AudioColumns._ID + " = ?"

    override fun getAllSongMetadata(): MutableList<MediaMetadataCompat> {
        val cursor = queryMediaStoreSongs()
        return extractSongsFromCursor(cursor)
    }

    private fun queryMediaStoreSongs(selection: String? = null, selectionArgs: Array<String>? = null): Cursor? {
        return mediaStore.query(
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL),
            songProjection,
            selection,
            selectionArgs,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
    }

    private fun extractSongsFromCursor(cursor: Cursor?): MutableList<MediaMetadataCompat> {
        val songs = arrayListOf<MediaMetadataCompat>()

        // if the cursor is not null
        cursor?.let { c ->
            // and the cursor has at least one entry
            if(c.moveToFirst()) {
                // iterate rows and create song objects
                do {
                    songs.add(getSongFromCursor(c))
                } while (c.moveToNext())
            }
        }

        return songs
    }

    private fun getSongFromCursor(cursor: Cursor): MediaMetadataCompat {
        val id = cursor.getString(MediaStore.Audio.AudioColumns._ID)
        val title = cursor.getString(MediaStore.Audio.AudioColumns.TITLE)
        val trackNumber = cursor.getLong(MediaStore.Audio.AudioColumns.TRACK)
        val year = cursor.getInt(MediaStore.Audio.AudioColumns.YEAR)
        val duration = cursor.getLong(MediaStore.Audio.AudioColumns.DURATION)
        val dateModified = cursor.getLong(MediaStore.Audio.AudioColumns.DATE_MODIFIED)
        val albumId = cursor.getLong(MediaStore.Audio.AudioColumns.ALBUM_ID)
        val albumName = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.ALBUM)
        val artistId = cursor.getLong(MediaStore.Audio.AudioColumns.ARTIST_ID)
        val artistName = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.ARTIST)
        val composer = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.COMPOSER)

        val metadataBuilder = MediaMetadataCompat.Builder()
        metadataBuilder.id = id
        metadataBuilder.title = title
        metadataBuilder.displayTitle = title
        metadataBuilder.displaySubtitle = artistName
        metadataBuilder.trackNumber = trackNumber
        metadataBuilder.duration = duration
        metadataBuilder.album = albumName
        metadataBuilder.artist = artistName
        metadataBuilder.displayIconUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId).toString()
        metadataBuilder.mediaUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toLong()).toString()
        metadataBuilder.albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId).toString()
        metadataBuilder.flag = FLAG_PLAYABLE

        return metadataBuilder.build()
    }
}