package dev.chapz.musichub.ui.songs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.chapz.musichub.databinding.ItemSongBinding
import dev.chapz.musichub.repository.Song

class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var songs: List<Song> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    public fun setData(songList: List<Song>) {
        songs = songList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemSongBinding = ItemSongBinding.inflate(layoutInflater)
        return ViewHolder(itemSongBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.songTitle.text = songs[position].title
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount() = songs.size

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root)
}