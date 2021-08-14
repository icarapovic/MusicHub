package dev.chapz.musichub.repository

import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
import android.support.v4.media.MediaMetadataCompat
import dev.chapz.musichub.service.flag
import dev.chapz.musichub.service.id
import dev.chapz.musichub.service.title
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

    fun getChildrenForRoot(root: String): MutableList<MediaMetadataCompat> {
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

    private fun buildMainRoot(): MutableList<MediaMetadataCompat> {
        val builder = MediaMetadataCompat.Builder()
        val songs = builder.apply {
            title = "Songs"
            id = SONG_ROOT
            flag = FLAG_BROWSABLE
        }.build()
        val albums = builder.apply {
            title = "Albums"
            id = ALBUM_ROOT
            flag = FLAG_BROWSABLE
        }.build()
        val artists = builder.apply {
            title = "Artists"
            id = ARTIST_ROOT
            flag = FLAG_BROWSABLE
        }.build()
        val genres = builder.apply {
            title = "Genres"
            id = GENRE_ROOT
            flag = FLAG_BROWSABLE
        }.build()
        val playlists = builder.apply {
            title = "Playlists"
            id = PLAYLIST_ROOT
            flag = FLAG_BROWSABLE
        }.build()

        return mutableListOf(songs, albums, artists, genres, playlists)
    }

    private fun buildSongsRoot(): MutableList<MediaMetadataCompat> {
        return songRepository.getAllSongMetadata()
    }

    private fun buildAlbumsRoot(): MutableList<MediaMetadataCompat> {
        return albumRepository.getAllAlbumsMetadata()
    }

    private fun buildArtistsRoot(): MutableList<MediaMetadataCompat> {
        return mutableListOf()
    }

    private fun buildGenresRoot(): MutableList<MediaMetadataCompat> {
        return mutableListOf()
    }

    private fun buildPlaylistRoot(): MutableList<MediaMetadataCompat> {
        return mutableListOf()
    }

    private fun parseAndBuildRoot() : MutableList<MediaMetadataCompat> {
        return mutableListOf()
    }

}