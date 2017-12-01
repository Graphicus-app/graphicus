package com.graphicus.graphicus.gui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.graphicus.graphicus.core.drawing.DrawingType
import com.graphicus.graphicus.core.drawing.LineData

class DrawingView : ImageView {

    private val touchTolerance: Float = 3.0f

    private val lineDataList: ArrayList<LineData> = ArrayList()

    private var prevX: Float = 0.0f
    private var prevY: Float = 0.0f

    var drawingType: DrawingType = DrawingType.Pen
    private var strokeWidth: Float = 4.0f
    @ColorInt
    private var strokeColor: Int = Color.BLACK

    private var bitmap: Bitmap? = null
    private lateinit var bitmapPaint: Paint
    private var canvas: Canvas? = null
    private lateinit var drawingPaint: Paint
    private lateinit var strokePath: Path

    private var drawingMoved: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        strokePath = Path()
        bitmapPaint = Paint(Paint.DITHER_FLAG)
        drawingPaint = Paint()
        drawingPaint.isAntiAlias = true
        drawingPaint.color = strokeColor
        drawingPaint.style = Paint.Style.STROKE
        drawingPaint.strokeJoin = Paint.Join.ROUND
        drawingPaint.strokeCap = Paint.Cap.ROUND
        drawingPaint.strokeWidth = strokeWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (bitmap == null || canvas == null) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBitmap(bitmap, 0f, 0f, bitmapPaint)

        if (drawingType != DrawingType.Eraser) {
            canvas?.drawPath(strokePath, drawingPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x: Float = event!!.x
        val y: Float = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchDown(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }

        return true
    }

    private fun touchDown(x: Float, y: Float) {
        strokePath = Path()

        val lineData = LineData(strokeColor, strokeWidth, strokePath)
        lineDataList.add(lineData)

        strokePath.moveTo(x, y)
        prevX = x
        prevY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - prevX)
        val dy = Math.abs(y - prevY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            drawingMoved = true

            strokePath.quadTo(prevX, prevY, (x + prevX) / 2, (y + prevY) / 2)

            prevX = x
            prevY = y

            canvas?.drawPath(strokePath, drawingPaint)
        }
    }

    private fun touchUp() {
        strokePath.lineTo(prevX, prevY)

        if (!drawingMoved) {
            strokePath.quadTo(prevX - 1, prevY - 1, prevX + 1, prevY + 1)
        } else {
            drawingMoved = false
        }

//        if (drawingType != DrawingType.Eraser) {
            canvas?.drawPath(strokePath, drawingPaint)
//        }

        strokePath.reset()
    }

    fun updateDrawingType(type: DrawingType) {
        drawingType = type

        if (drawingType == DrawingType.Eraser) {
            drawingPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else {
            drawingPaint.xfermode = null
        }
    }

    fun updateColor(@ColorInt color: Int) {
        if (strokeColor != color) {
            strokeColor = color
            drawingPaint.color = color
        }
    }

    fun updateStrokeWidth(width: Float) {
        if (strokeWidth != width) {
            strokeWidth = width
            drawingPaint.strokeWidth = width
        }
    }

    fun fillColor(color: Int){
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas?.drawColor(color)
        invalidate()
        redrawLines()
    }

    private fun redrawLines() {
        val oldPaint = drawingPaint

        for (item in lineDataList) {
            drawingPaint.color = item.lineColor
            drawingPaint.strokeWidth = item.strokeWidth
            canvas?.drawPath(item.strokePath, drawingPaint)
        }

        drawingPaint = oldPaint

        invalidate()
    }
}