package dev.chapz.musichub.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.chapz.musichub.databinding.FragmentAlbumsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumsFragment : Fragment() {

    private lateinit var ui : FragmentAlbumsBinding
    private lateinit var albumAdapter: AlbumAdapter

    private val viewModel: AlbumViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ui = FragmentAlbumsBinding.inflate(inflater)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        albumAdapter = AlbumAdapter()
        albumAdapter.setHasStableIds(true)

        ui.albumRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = albumAdapter
        }

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            albumAdapter.setData(albums)
        }
    }
}