package com.graphicus.graphicus

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gordonwong.materialsheetfab.MaterialSheetFab
import com.graphicus.graphicus.gui.views.CustomFloatingActionButton
import com.graphicus.graphicus.utility.FileUtility
import kotlinx.android.synthetic.main.home_screen_layout.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class HomeScreenActivity : AppCompatActivity() {

    companion object {
        val CAMERA_AND_STORAGE = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        const val RC_CAMERA_PERMISSION: Int = 123
        const val RC_WRITE_STORAGE_PERMISSION = 124
        const val CAMERA_INTENT = 1000
        const val FILE_INTENT = 1001
    }

    private lateinit var materialSheetFab: MaterialSheetFab<CustomFloatingActionButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.app_name)
        setContentView(R.layout.home_screen_layout)
        materialSheetFab = setupFloatingActionButton()
        materialSheetFab.showFab()
    }

    override fun onBackPressed() {
        if (materialSheetFab.isSheetVisible) {
            materialSheetFab.hideSheet()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_INTENT && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, MainActivity::class.java)
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap
            intent.putExtra("photo", bitmap)
            startActivity(intent)
        } else if (requestCode == FILE_INTENT && resultCode == Activity.RESULT_OK) {
            val filePath = FileUtility.getPathFromUri(this, data?.data)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("filePath", filePath)
            startActivity(intent)
        }
    }

    private fun setupFloatingActionButton(): MaterialSheetFab<CustomFloatingActionButton> {
        val fab = new_project_button
        val sheetView = fab_sheet
        val overlay = fab_overlay
        val sheetColor = ContextCompat.getColor(this, R.color.background_card)
        val fabColor = ContextCompat.getColor(this, R.color.colorAccent)

        return MaterialSheetFab(fab, sheetView, overlay, sheetColor, fabColor)
    }

    fun createEmptyProject(@Suppress("UNUSED_PARAMETER") v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @AfterPermissionGranted(RC_CAMERA_PERMISSION)
    fun createProjectFromCamera(@Suppress("UNUSED_PARAMETER") v: View) {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, CAMERA_INTENT)
            }
        } else {
            EasyPermissions.requestPermissions(this, "Please :)", RC_CAMERA_PERMISSION, Manifest.permission.CAMERA)
        }
    }

    @AfterPermissionGranted(RC_WRITE_STORAGE_PERMISSION)
    fun createProjectFromFile(@Suppress("UNUSED_PARAMETER") v: View) {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, FILE_INTENT)
        } else {
            EasyPermissions.requestPermissions(this, "Please :)", RC_WRITE_STORAGE_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

}