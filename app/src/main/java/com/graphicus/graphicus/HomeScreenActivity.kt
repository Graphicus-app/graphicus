package com.graphicus.graphicus

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gordonwong.materialsheetfab.MaterialSheetFab
import com.graphicus.graphicus.gui.views.CustomFloatingActionButton
import kotlinx.android.synthetic.main.home_screen_layout.*

class HomeScreenActivity : AppCompatActivity() {

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

    private fun setupFloatingActionButton(): MaterialSheetFab<CustomFloatingActionButton> {
        val fab = new_project_button
        val sheetView = fab_sheet
        val overlay = fab_overlay
        val sheetColor = ContextCompat.getColor(this, R.color.background_card)
        val fabColor = ContextCompat.getColor(this, R.color.colorAccent)

        return MaterialSheetFab(fab, sheetView, overlay, sheetColor, fabColor)
    }

    fun createProject(@Suppress("UNUSED_PARAMETER") v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}