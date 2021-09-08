package dev.chapz.musichub.service

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData

class MediaServiceConnection(private val context: Context) {

    companion object {
        val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
            .build()
        val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, Uri.EMPTY.toString())
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, Uri.EMPTY.toString())
            .build()
    }

    val isConnected = MutableLiveData<Boolean>().apply { postValue(false) }
    val rootMediaId: String get() = mediaBrowser.root
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls
    val playbackState = MutableLiveData<PlaybackStateCompat>().apply { postValue(EMPTY_PLAYBACK_STATE) }
    val nowPlaying = MutableLiveData<MediaMetadataCompat>().apply { postValue(NOTHING_PLAYING) }

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback()
    private val serviceComponent = ComponentName(context, MediaService::class.java)
    private var mediaBrowser = MediaBrowserCompat(context, serviceComponent, mediaBrowserConnectionCallback, null).apply { connect() }
    private lateinit var mediaController: MediaControllerCompat

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            isConnected.postValue(true)
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply { registerCallback(MediaControllerCallback()) }
        }
        override fun onConnectionSuspended() = isConnected.postValue(false)
        override fun onConnectionFailed() = isConnected.postValue(false)
    }

    private inner class MediaControllerCallback: MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) = playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) = nowPlaying.postValue(if(metadata?.id == null) NOTHING_PLAYING else metadata)
        override fun onSessionDestroyed() = mediaBrowserConnectionCallback.onConnectionSuspended()
    }
}