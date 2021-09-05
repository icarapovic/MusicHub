package dev.chapz.musichub.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.chapz.musichub.R
import dev.chapz.musichub.databinding.ActivityHostBinding
import dev.chapz.musichub.service.isPlaying
import dev.chapz.musichub.service.stateName
import org.koin.androidx.viewmodel.ext.android.viewModel

class HostActivity : AppCompatActivity() {

    private lateinit var ui: ActivityHostBinding
    private val viewModel: HostViewModel by viewModel()
    private lateinit var navHost: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ActivityHostBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.songs -> {
                    navHost.navigate(R.id.songsFragment)
                    true
                }
                R.id.albums -> {
                    navHost.navigate(R.id.albumsFragment)
                    true
                }
                else -> false
            }
        }

        checkPermissions()
    }

    override fun onStart() {
        super.onStart()
        navHost = findNavController(ui.navHostFragment.id)

        ui.next.setOnClickListener {
            viewModel.transportControls.skipToNext()
        }

        ui.previous.setOnClickListener {
            viewModel.transportControls.skipToPrevious()
        }

        ui.playPause.setOnClickListener {
            viewModel.playbackState.value?.let {
                if (it.isPlaying) {
                    viewModel.transportControls.pause()
                } else {
                    viewModel.transportControls.play()
                }
            }
        }

        viewModel.playbackState.observe(this) {
            Log.d("---", "Playback state: ${it.stateName}")
            if (it.isPlaying) {
                ui.playPause.setImageResource(R.drawable.ic_pause)
            } else {
                ui.playPause.setImageResource(R.drawable.ic_play)
            }
        }
    }

    private fun checkPermissions() {
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            return
        }

        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Permission needed")
                .setMessage("In order to list your songs, we need the permission to read them from your phones storage.")
                .setPositiveButton("Grant") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 101)
                }.setNegativeButton("Deny") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }.create().show()
        } else {
            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults.first() == PERMISSION_DENIED) {
            checkPermissions()
        } else if (requestCode == 101 && grantResults.first() == PERMISSION_DENIED) {
            // display info + button in UI
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}