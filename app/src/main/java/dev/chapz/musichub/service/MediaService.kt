package dev.chapz.musichub.service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import dev.chapz.musichub.client.MediaEngine
import dev.chapz.musichub.client.MediaEngineImpl

class MediaService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionCallback: MediaSessionCompat.Callback
    private lateinit var engine: MediaEngine

    override fun onCreate() {
        super.onCreate()

        // create a new MediaSession and assign the token
        mediaSession = MediaSessionCompat(this,"MediaService")
        sessionToken = mediaSession.sessionToken

        // set media session callback handler
        mediaSessionCallback = MediaSessionCallback()
        mediaSession.setCallback(mediaSessionCallback)

        engine = MediaEngineImpl(this)
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        val extras = Bundle()
        return BrowserRoot("ROOT", extras)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.detach()
    }

    override fun onDestroy() {
        mediaSession.release()
        super.onDestroy()
    }
}