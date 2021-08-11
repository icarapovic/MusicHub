package dev.chapz.musichub.ui.albums

import android.annotation.SuppressLint
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.chapz.musichub.databinding.ItemAlbumBinding

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private var albums: List<MediaItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    public fun setData(albumList: List<MediaItem>) {
        albums = albumList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemAlbumBinding = ItemAlbumBinding.inflate(layoutInflater)
        return ViewHolder(itemAlbumBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.albumTitle.text = albums[position].description.title
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount() = albums.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root)
}