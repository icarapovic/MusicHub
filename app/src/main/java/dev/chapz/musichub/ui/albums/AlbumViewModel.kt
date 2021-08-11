package dev.chapz.musichub.ui.albums

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dev.chapz.musichub.repository.MediaManager
import dev.chapz.musichub.service.MediaServiceConnection

class AlbumViewModel(private val mediaServiceConnection: MediaServiceConnection) : ViewModel() {

    val albums = MutableLiveData<List<MediaItem>>().apply { value = emptyList() }

    private val connectionObserver = Observer<Boolean> { isConnected ->
        if(isConnected) {
            mediaServiceConnection.subscribe(MediaManager.ALBUM_ROOT, object: MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(parentId: String, children: MutableList<MediaItem>) {
                    albums.postValue(children)
                }
            })
        }
    }

    init {
        mediaServiceConnection.isConnected.observeForever(connectionObserver)
    }

    override fun onCleared() {
        mediaServiceConnection.isConnected.removeObserver(connectionObserver)
    }

}