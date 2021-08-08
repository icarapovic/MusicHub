package dev.chapz.musichub

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.chapz.musichub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var ui: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = ActivityMainBinding.inflate(layoutInflater)
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
            Toast.makeText(this, "First & denied", Toast.LENGTH_SHORT).show()
        } else if(requestCode == 101 && grantResults.first() == PERMISSION_DENIED) {
            // display info + button in UI
            Toast.makeText(this, "Rationale & denied", Toast.LENGTH_SHORT).show()
        } else if(grantResults.first() == PERMISSION_GRANTED) {
            // do continue
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}