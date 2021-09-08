package dev.chapz.musichub.ui.songs

import android.annotation.SuppressLint
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.chapz.musichub.databinding.ItemSongBinding

class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var songs: List<MediaItem> = emptyList()
    private var listener: (mediaItem: MediaItem) -> Unit = { }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(songList: List<MediaItem>) {
        songs = songList
        notifyDataSetChanged()
    }

    fun onMediaItemClicked(listener: (mediaItem: MediaItem) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemSongBinding = ItemSongBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemSongBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            title.text = songs[position].description.title
            subtitle.text = songs[position].description.subtitle
            Glide.with(albumArt)
                .load(songs[position].description.iconUri)
                .override(64, 64) // TODO optimize with preloader
                .into(albumArt)
            albumArt.setImageURI(songs[position].description.iconUri)
            root.setOnClickListener { listener.invoke(songs[position]) }
        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount() = songs.size

    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root)
}