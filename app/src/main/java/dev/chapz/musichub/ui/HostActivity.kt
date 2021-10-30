package dev.chapz.musichub.ui

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.composethemeadapter.MdcTheme
import dev.chapz.musichub.R
import dev.chapz.musichub.ui.theme.MusicHubTheme

class HostActivity : AppCompatActivity() {

//    private lateinit var ui: ActivityHostBinding
//    private val viewModel: HostViewModel by viewModel()
//    private lateinit var navHost: NavController

    @ExperimentalMaterial3Api
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }

//        checkPermissions()
    }

    @ExperimentalMaterial3Api
    @Preview
    @Composable
    fun MainContent() {

        MusicHubTheme {
            val systemUiController = rememberSystemUiController()
            val colorSurface = MaterialTheme.colors.surface

            SideEffect {

                systemUiController.setSystemBarsColor(Color.Gray)
            }

            Scaffold(
                containerColor = Color.DarkGray,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "MusicHub",
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    Toast.makeText(this, "click", Toast.LENGTH_LONG).show()
                                },
                                content = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_album),
                                        contentDescription = "",
                                    )
                                }
                            )
                        },
                        contentColor = Color.DarkGray,
                        elevation = 4.dp
                    )
                }, content = {

                }
            )
        }
    }


//    override fun onStart() {
//        super.onStart()
//        navHost = findNavController(ui.navHostFragment.id)
//
//        ui.playPause.setOnClickListener {
//            viewModel.playbackState.value?.let {
//                if (it.isPlaying) {
//                    viewModel.transportControls.pause()
//                } else {
//                    viewModel.transportControls.play()
//                }
//            }
//        }
//
//        viewModel.playbackState.observe(this) {
//            Log.d("---", "Playback state: ${it.stateName}")
//            if (it.isPlaying) {
//                ui.playPause.setImageResource(R.drawable.ic_pause)
//            } else {
//                ui.playPause.setImageResource(R.drawable.ic_play)
//            }
//        }
//
//        viewModel.nowPlaying.observe(this) { metadata ->
//            if(metadata != NOTHING_PLAYING) {
//                ui.playbackControls.visibility = View.VISIBLE
//
//                if(metadata.displayIconUri != Uri.EMPTY) {
//                    ui.albumArt.setImageURI(metadata.displayIconUri)
//                }
//
//                ui.title.text = metadata.displayTitle
//                ui.artist.text = metadata.displaySubtitle
//            }
//        }
//    }
//
//    private fun checkPermissions() {
//        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
//            viewModel.connectMediaService()
//            return
//        }
//
//        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
//            MaterialAlertDialogBuilder(this)
//                .setTitle("Permission needed")
//                .setMessage("In order to list your songs, we need the permission to read them from your phones storage.")
//                .setPositiveButton("Grant") { dialogInterface, _ ->
//                    dialogInterface.dismiss()
//                    requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 101)
//                }.setNegativeButton("Deny") { dialogInterface, _ ->
//                    dialogInterface.dismiss()
//                }.create().show()
//        } else {
//            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 100)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == 100 && grantResults.first() == PERMISSION_DENIED) {
//            checkPermissions()
//        } else if (requestCode == 101 && grantResults.first() == PERMISSION_DENIED) {
//            // display info + button in UI
//        } else if(grantResults.first() == PERMISSION_GRANTED) {
//            viewModel.connectMediaService()
//        }
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
}