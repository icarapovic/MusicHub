package dev.chapz.musichub.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.chapz.musichub.databinding.FragmentSongsBinding
import dev.chapz.musichub.ui.HostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SongsFragment : Fragment() {

    private lateinit var ui: FragmentSongsBinding
    private lateinit var songAdapter: SongAdapter

    private val viewModel: SongViewModel by viewModel()
    private val hostViewModel: HostViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ui = FragmentSongsBinding.inflate(inflater)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        songAdapter = SongAdapter()
        songAdapter.setHasStableIds(true)
        songAdapter.onMediaItemClicked { mediaItem ->
            hostViewModel.playMediaItem(mediaItem)
        }

        ui.songRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = songAdapter
        }

        viewModel.songs.observe(viewLifecycleOwner) { songs ->
            songAdapter.setData(songs)
        }
    }
}