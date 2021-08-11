package dev.chapz.musichub.repository

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MediaManager: KoinComponent {

    private val songRepository: SongRepository by inject()
    private val albumRepository: AlbumRepository by inject()

    companion object {
        const val ROOT = "content://root/"
        const val SONG_ROOT = "content://songs/"
        const val ALBUM_ROOT = "content://albums/"
        const val ARTIST_ROOT = "content://artists/"
        const val GENRE_ROOT = "content://genres/"
        const val PLAYLIST_ROOT = "content://playlists/"
    }

    fun getChildrenForRoot(root: String): MutableList<MediaItem> {
        return when(root) {
            ROOT -> buildMainRoot()
            SONG_ROOT -> buildSongsRoot()
            ALBUM_ROOT -> buildAlbumsRoot()
            ARTIST_ROOT -> buildArtistsRoot()
            GENRE_ROOT -> buildGenresRoot()
            PLAYLIST_ROOT -> buildPlaylistRoot()
            else -> parseAndBuildRoot()
        }

    }

    private fun buildMainRoot(): MutableList<MediaItem> {
        val builder = MediaDescriptionCompat.Builder()
        val songs = MediaItem(builder.setTitle("Songs").setMediaId(SONG_ROOT).build(), MediaItem.FLAG_BROWSABLE)
        val albums = MediaItem(builder.setTitle("Albums").setMediaId(ALBUM_ROOT).build(), MediaItem.FLAG_BROWSABLE)
        val artists = MediaItem(builder.setTitle("Artists").setMediaId(ARTIST_ROOT).build(), MediaItem.FLAG_BROWSABLE)
        val genres = MediaItem(builder.setTitle("Genres").setMediaId(GENRE_ROOT).build(), MediaItem.FLAG_BROWSABLE)
        val playlists = MediaItem(builder.setTitle("Playlists").setMediaId(PLAYLIST_ROOT).build(), MediaItem.FLAG_BROWSABLE)

        return mutableListOf(songs, albums, artists, genres, playlists)
    }

    private fun buildSongsRoot(): MutableList<MediaItem> {
        return songRepository.getAllSongs()
    }

    private fun buildAlbumsRoot(): MutableList<MediaItem> {
        return albumRepository.getAllAlbums()
    }

    private fun buildArtistsRoot(): MutableList<MediaItem> {
        return mutableListOf()
    }

    private fun buildGenresRoot(): MutableList<MediaItem> {
        return mutableListOf()
    }

    private fun buildPlaylistRoot(): MutableList<MediaItem> {
        return mutableListOf()
    }

    private fun parseAndBuildRoot() : MutableList<MediaItem> {
        return mutableListOf()
    }

}