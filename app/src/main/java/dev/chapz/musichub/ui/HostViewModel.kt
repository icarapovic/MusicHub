package dev.chapz.musichub.ui

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.ViewModel
import dev.chapz.musichub.service.MediaServiceConnection

class HostViewModel(private val mediaServiceConnection: MediaServiceConnection): ViewModel() {

    fun connectService() {
        mediaServiceConnection.subscribe("parentRoot", object: MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
                Log.d("---", "loaded")
            }

            override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>, options: Bundle) {
                Log.d("---", "loaded bundle")
            }

            override fun onError(parentId: String, options: Bundle) {
                Log.d("---", "error")
            }

            override fun onError(parentId: String) {
                Log.d("---", "error bundle")
            }
        })
    }
}