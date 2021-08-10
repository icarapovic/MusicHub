package dev.chapz.musichub.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.chapz.musichub.databinding.ActivityHostBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HostActivity : AppCompatActivity() {

    private lateinit var ui: ActivityHostBinding
    private val hostViewModel: HostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ActivityHostBinding.inflate(layoutInflater)
        setContentView(ui.root)

        checkPermissions()
    }

    private fun checkPermissions() {
        if(checkSelfPermission(READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            return
        }

        if(shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Permission needed")
                .setMessage("In order to list your songs, we need the permission to read them from your phones storage.")
                .setPositiveButton("Grant") {  dialogInterface, _ ->
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
        if(requestCode == 100 && grantResults.first() == PERMISSION_DENIED) {
            checkPermissions()
        } else if(requestCode == 101 && grantResults.first() == PERMISSION_DENIED) {
            // display info + button in UI
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}