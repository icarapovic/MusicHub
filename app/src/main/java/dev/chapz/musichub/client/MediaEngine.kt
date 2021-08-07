package dev.chapz.musichub.client

interface MediaEngine {

    /**
     * The playback service has been destroyed,
     * release the resources used by the player
     * */
    fun onDestroy()
}