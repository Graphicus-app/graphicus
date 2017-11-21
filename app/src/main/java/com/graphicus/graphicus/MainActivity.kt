package com.graphicus.graphicus

import android.content.res.ColorStateList
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
        colorBlueButton.setOnClickListener(this)
        colorRedButton.setOnClickListener(this)
        eraserTypeButton.setOnClickListener(this)

        strokeWidthSeekBar.onProgressChangedListener = this
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.colorBlackButton -> showColorPickerDialog(view.id)
            R.id.colorBlueButton  -> showColorPickerDialog(view.id)
            R.id.colorRedButton   -> showColorPickerDialog(view.id)
            R.id.eraserTypeButton -> drawingView.updateDrawingType(DrawingType.Eraser)
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
            R.id.colorBlueButton  -> {
                colorBlueButton.backgroundTintList = colorStateList
            }
            R.id.colorRedButton   -> {
                colorRedButton.backgroundTintList = colorStateList
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
