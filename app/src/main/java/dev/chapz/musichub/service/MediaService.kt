package dev.chapz.musichub.service

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dev.chapz.musichub.repository.MediaManager
import dev.chapz.musichub.repository.MediaManager.Companion.ROOT
import dev.chapz.musichub.repository.MediaManager.Companion.SONG_ROOT
import org.koin.android.ext.android.inject

open class MediaService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private val mediaManager: MediaManager by inject()

    private val audioAttrs = AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA).build()
    private val player: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(audioAttrs, true)
            setHandleAudioBecomingNoisy(true)
        }
    }
    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(this, Util.getUserAgent(this, "MediaService"))
    }
    private lateinit var mediaSessionConnector: MediaSessionConnector

    override fun onCreate() {
        super.onCreate()

        // Intent to launch the main activity of this app
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        mediaSession = MediaSessionCompat(this, "MediaService").apply {
            setSessionActivity(launchIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(object : MediaSessionConnector.PlaybackPreparer {
            override fun onCommand(player: Player, controlDispatcher: ControlDispatcher, command: String, extras: Bundle?, cb: ResultReceiver?) = false
            override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit
            override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

            override fun getSupportedPrepareActions(): Long =
                PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                        PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

            override fun onPrepare(playWhenReady: Boolean) {
                Log.d("---", "onPrepare: playWhenReady: $playWhenReady")
            }

            override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
                Log.d("---", "onPrepareFromMediaId")
                player.playWhenReady = playWhenReady
                player.stop()
                player.clearMediaItems()

                val mediaMetadata = mediaManager.getChildrenForRoot(SONG_ROOT).find { it.id == mediaId }!!
                val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(mediaMetadata.mediaUri)
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
                player.setMediaSource(mediaSource)
                player.prepare()
            }
        })
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot {
        return BrowserRoot(ROOT, rootHints)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        val mediaItems = mediaManager.getChildrenForRoot(parentId).map { MediaItem(it.description, it.flag) }.toMutableList()
        result.sendResult(mediaItems)
    }

    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }

        player.release()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        player.stop()
        player.clearMediaItems()
    }
}