package dev.chapz.musichub.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.chapz.musichub.databinding.FragmentSongsBinding
import dev.chapz.musichub.repository.SongRepository
import dev.chapz.musichub.repository.SongRepositoryImpl

class SongsFragment : Fragment() {

    private lateinit var ui: FragmentSongsBinding
    private lateinit var songAdapter: SongAdapter
    private lateinit var songRepository: SongRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ui = FragmentSongsBinding.inflate(inflater)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        songRepository = SongRepositoryImpl(requireContext().contentResolver)

        songAdapter = SongAdapter()
        songAdapter.setHasStableIds(true)
        songAdapter.setData(songRepository.getAllSongs())

        ui.songRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = songAdapter
        }
    }
}