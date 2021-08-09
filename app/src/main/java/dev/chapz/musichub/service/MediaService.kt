package dev.chapz.musichub.service

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat

open class MediaService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionCallback: MediaSessionCompat.Callback

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
        val extras = Bundle()
        Log.d("---", "return root")
        return BrowserRoot("service root", extras)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        Log.d("---", "onLoadChildren, parentId: $parentId")
        result.sendResult(mutableListOf())
    }

    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }
    }
}