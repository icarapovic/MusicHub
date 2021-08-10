package dev.chapz.musichub.service

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import dev.chapz.musichub.repository.MediaManager
import dev.chapz.musichub.repository.MediaManager.Companion.ROOT

open class MediaService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionCallback: MediaSessionCompat.Callback
    private val mediaManager = MediaManager()

    override fun onCreate() {
        super.onCreate()

        // Intent to launch the main activity of this app
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        mediaSessionCallback = MediaSessionCallback()

        mediaSession = MediaSessionCompat(this, "MediaService").apply {
            setCallback(mediaSessionCallback)
            setSessionActivity(launchIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        return BrowserRoot(ROOT, rootHints)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        result.sendResult(mediaManager.getChildrenForRoot(parentId))
    }

    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }
    }
}