package com.graphicus.graphicus

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.graphicus.graphicus.core.drawing.DrawingType
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawing_toolbar_layout.*


class MainActivity : AppCompatActivity(), View.OnClickListener, ColorPickerDialogListener, BubbleSeekBar.OnProgressChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        colorBlackButton.setOnClickListener(this)
        colorButton.setOnClickListener(this)
        eraserTypeButton.setOnClickListener(this)
        menuButton.setOnClickListener(this)
        strokeWidthSeekBar.onProgressChangedListener = this

        if (intent.extras != null) {
            var bitmap: Bitmap? = null

            if (intent.extras.get("photo") != null) {
                bitmap = intent.extras.get("photo") as Bitmap
            } else if (intent.getStringExtra("filePath") != null) {
                bitmap = BitmapFactory.decodeFile(intent.getStringExtra("filePath"))
            }

            drawingView.setImageBitmap(bitmap)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.colorBlackButton -> showColorPickerDialog(view.id)
            R.id.colorButton  -> showColorPickerDialog(view.id)
            R.id.eraserTypeButton -> drawingView.updateDrawingType(DrawingType.Eraser)
            R.id.menuButton -> {
                //Fragment with main menu R.id.menu.menu TO-DO: Ola

            }
        }
    }

    override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
        drawingView.updateStrokeWidth(progressFloat)
    }

    override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {}

    override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {}


    override fun onColorSelected(dialogId: Int, @ColorInt color: Int) {
        val colorStateList: ColorStateList = ColorStateList.valueOf(color)

        when (dialogId) {
            R.id.colorBlackButton -> {
                colorBlackButton.backgroundTintList = colorStateList
            }
            R.id.colorButton -> { drawingView.fillColor(color)
            }
        }

        drawingView.updateColor(color)

        if (drawingView.drawingType == DrawingType.Eraser) {
            drawingView.updateDrawingType(DrawingType.Pen)
        }
    }

    override fun onDialogDismissed(dialogId: Int) {}

    private fun showColorPickerDialog(@IdRes viewId: Int) {
        ColorPickerDialog.newBuilder().setDialogId(viewId).setShowAlphaSlider(true).show(this)
    }

}
