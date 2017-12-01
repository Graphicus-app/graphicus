package com.graphicus.graphicus.core.drawing

import android.graphics.Path
import android.support.annotation.ColorInt

data class LineData(@ColorInt val lineColor: Int, val strokeWidth: Float, val strokePath: Path)