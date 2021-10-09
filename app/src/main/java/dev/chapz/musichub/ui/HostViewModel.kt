package dev.chapz.musichub.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.chapz.musichub.service.*

class HostViewModel(private val mediaServiceConnection: MediaServiceConnection) : ViewModel() {

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaServiceConnection.transportControls
    val playbackState: MutableLiveData<PlaybackStateCompat>
        get() = mediaServiceConnection.playbackState
    val nowPlaying: MutableLiveData<MediaMetadataCompat>
        get() = mediaServiceConnection.nowPlaying

    fun connectMediaService() {
        mediaServiceConnection.connect()
    }

    fun playMediaItem(mediaItem: MediaBrowserCompat.MediaItem) {
        Log.d("---", "Clicked on: ${mediaItem.description.title}")

        if (mediaItem.isBrowsable) {

        } else {
            val nowPlaying = mediaServiceConnection.nowPlaying.value
            val transportControls = mediaServiceConnection.transportControls
            val isPrepared = mediaServiceConnection.playbackState.value?.isPrepared ?: false

            if (isPrepared && mediaItem.mediaId == nowPlaying?.id) {
                mediaServiceConnection.playbackState.value?.let { playbackState ->
                    when {
                        playbackState.isPlaying -> transportControls.pause()
                        playbackState.isPlayEnabled -> transportControls.play()
                        else -> {
                            Log.d("---", "playbackState = else")
                        }
                    }
                }
            } else {
                transportControls.playFromMediaId(mediaItem.mediaId, null)
            }
        }
    }
}