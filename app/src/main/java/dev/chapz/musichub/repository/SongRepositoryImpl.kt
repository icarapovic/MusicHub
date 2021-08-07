package dev.chapz.musichub.repository

import android.content.ContentResolver
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore

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

    override fun getAllSongs(): List<Song> {
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

    private fun extractSongsFromCursor(cursor: Cursor?): List<Song> {
        val songs = arrayListOf<Song>()

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

    private fun getSongFromCursor(cursor: Cursor): Song {
        val id = cursor.getLong(MediaStore.Audio.AudioColumns._ID)
        val title = cursor.getString(MediaStore.Audio.AudioColumns.TITLE)
        val trackNumber = cursor.getInt(MediaStore.Audio.AudioColumns.TRACK)
        val year = cursor.getInt(MediaStore.Audio.AudioColumns.YEAR)
        val duration = cursor.getLong(MediaStore.Audio.AudioColumns.DURATION)
        val dateModified = cursor.getLong(MediaStore.Audio.AudioColumns.DATE_MODIFIED)
        val albumId = cursor.getLong(MediaStore.Audio.AudioColumns.ALBUM_ID)
        val albumName = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.ALBUM)
        val artistId = cursor.getLong(MediaStore.Audio.AudioColumns.ARTIST_ID)
        val artistName = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.ARTIST)
        val composer = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.COMPOSER)

        return Song(id,
            title,
            trackNumber,
            year,
            duration,
            dateModified,
            albumId,
            albumName ?: "",
            artistId,
            artistName ?: "",
            composer ?: "")
    }
}