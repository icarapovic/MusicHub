package dev.chapz.musichub.client

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer

class MediaEngineImpl(context: Context): MediaEngine {

    private val player: ExoPlayer = SimpleExoPlayer.Builder(context).build()

    override fun onDestroy() {

    }
}