package dev.chapz.musichub.ui

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.ViewModel
import dev.chapz.musichub.service.*

class HostViewModel(private val mediaServiceConnection: MediaServiceConnection) : ViewModel() {

    fun playMediaItem(mediaItem: MediaBrowserCompat.MediaItem) {
        Log.d("---", "Clicked on: ${mediaItem.description.title}")

        if(mediaItem.isBrowsable) {

        } else {
            val nowPlaying = mediaServiceConnection.nowPlaying.value
            val transportControls = mediaServiceConnection.transportControls
            val isPrepared = mediaServiceConnection.playbackState.value?.isPrepared ?: false

            if(isPrepared && mediaItem.mediaId == nowPlaying?.id) {
                mediaServiceConnection.playbackState.value?.let { playbackState ->
                    when {
                        playbackState.isPlaying -> transportControls.pause()
                        playbackState.isPlayEnabled -> transportControls.play()
                        else -> { Log.d("---", "playbackState = else")}
                    }
                }
            } else {
                transportControls.playFromMediaId(mediaItem.mediaId, null)
            }
        }
    }
}