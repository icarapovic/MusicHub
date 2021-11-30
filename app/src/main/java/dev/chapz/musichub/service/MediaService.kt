package dev.chapz.musichub.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dev.chapz.musichub.repository.MediaManager
import dev.chapz.musichub.repository.MediaManager.Companion.ROOT
import dev.chapz.musichub.repository.MediaManager.Companion.SONG_ROOT
import org.koin.android.ext.android.inject
import dev.chapz.musichub.R.string as Strings

open class MediaService : MediaBrowserServiceCompat(),
    Player.Listener,
    PlayerNotificationManager.NotificationListener,
    MediaSessionConnector.PlaybackPreparer
{

    companion object {
        const val TAG = "::MediaService"
        const val HANDLE_NOISY = true
        const val HANDLE_AUDIO_FOCUS = true
        const val REQ_CODE = 1337
    }

    /** Properties */

    private var playWhenReady = true
    private var isForegroundService = false

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager : MediaNotificationManager
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private val mediaManager: MediaManager by inject()
    private var currentPlaylist: List<MediaMetadataCompat> = emptyList()

    private val audioAttrs = AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA).build()

    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(applicationContext).build().apply {
            setAudioAttributes(audioAttrs, HANDLE_AUDIO_FOCUS)
            setHandleAudioBecomingNoisy(HANDLE_NOISY)
            addListener(this@MediaService)
        }
    }

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSource.Factory(applicationContext)
    }

    /** Service lifecycle methods */

    override fun onCreate() {
        super.onCreate()

        // Intent to launch the main activity of this app
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName).let { intent ->
            PendingIntent.getActivity(applicationContext, REQ_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        mediaSession = MediaSessionCompat(applicationContext, TAG).apply {
            setSessionActivity(launchIntent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(this)
        mediaSessionConnector.setQueueNavigator(QueueNavigator(mediaSession))
        mediaSessionConnector.setPlayer(player)

        notificationManager = MediaNotificationManager(applicationContext, mediaSession.sessionToken, this)
        notificationManager.showNotificationForPlayer(player)
    }

    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }

        player.release()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        player.stop()
        player.clearMediaItems()
    }

    /** Browser service callbacks */

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?) = BrowserRoot(ROOT, rootHints)

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        val mediaItems = mediaManager.getChildrenForRoot(parentId).map { MediaItem(it.description, it.flag) }.toMutableList()
        result.sendResult(mediaItems)
    }

    /** Player.Listener methods */

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        Log.d(TAG, "Set playWhenReady = $playWhenReady")
        this.playWhenReady = playWhenReady
    }

    override fun onPlaybackStateChanged(state: Int) {
        Log.d(TAG, "Playback state: $state")
        when (state) {
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                notificationManager.showNotificationForPlayer(player)
                if (state == Player.STATE_READY) {
                    if (!playWhenReady) {
                        stopForeground(false)
                        isForegroundService = false
                    }
                }
            }
            else -> {
                notificationManager.hideNotification()
            }
        }
    }

    /** PlayerNotificationManager.NotificationListener methods */

    override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
        if (ongoing && !isForegroundService) {
            ContextCompat.startForegroundService(
                applicationContext,
                Intent(applicationContext, MediaService::class.java)
            )

            startForeground(notificationId, notification)
            isForegroundService = true
        }
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        stopForeground(true)
        isForegroundService = false
        stopSelf()
    }

    /** MediaSessionConnector.PlaybackPreparer methods */

    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ) = false

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
    }

    override fun onPrepare(playWhenReady: Boolean) {
        Log.d(TAG, "onPrepare: playWhenReady: $playWhenReady")
    }

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        Log.d(TAG, "onPrepareFromMediaId")
        player.playWhenReady = playWhenReady
        player.stop()
        player.clearMediaItems()

        val mediaMetadata = mediaManager.getChildrenForRoot(SONG_ROOT)
        val index = mediaMetadata.indexOfFirst { it.id == mediaId }
        preparePlaylist(mediaMetadata, mediaMetadata[index])
    }

    /** Queue Navigator implementation */

    inner class QueueNavigator(mediaSession: MediaSessionCompat): TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return currentPlaylist[windowIndex].description
        }
    }

    /**  Private methods */

    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
    ) {
        val initialIndex = if(itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)
        currentPlaylist = metadataList
        val mediaSource = metadataList.toMediaSource(dataSourceFactory)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.seekTo(initialIndex, 0)
    }
}