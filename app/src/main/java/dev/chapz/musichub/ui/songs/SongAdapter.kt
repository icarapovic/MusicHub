package dev.chapz.musichub.ui.songs

import android.annotation.SuppressLint
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.chapz.musichub.databinding.ItemSongBinding

class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var songs: List<MediaItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    public fun setData(songList: List<MediaItem>) {
        songs = songList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemSongBinding = ItemSongBinding.inflate(layoutInflater)
        return ViewHolder(itemSongBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.songTitle.text = songs[position].description.title
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount() = songs.size

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root)
}