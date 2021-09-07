package dev.chapz.musichub.service

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ForwardingPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dev.chapz.musichub.R

class MediaNotificationManager(
    context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

    companion object {
        const val NOW_PLAYING_CHANNEL_ID = "com.example.android.uamp.media.NOW_PLAYING"
        const val NOW_PLAYING_NOTIFICATION_ID = 0xb339
    }

    private val notificationManager = PlayerNotificationManager.Builder(
        context,
        NOW_PLAYING_NOTIFICATION_ID,
        NOW_PLAYING_CHANNEL_ID
    ).apply {
        setNotificationListener(notificationListener)
        setChannelNameResourceId(R.string.channel_desc)
    }.build().apply {
        setMediaSessionToken(sessionToken)
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player) {
        val forwardingPlayer = ForwardingPlayer(player)
        notificationManager.setPlayer(forwardingPlayer)
    }
}