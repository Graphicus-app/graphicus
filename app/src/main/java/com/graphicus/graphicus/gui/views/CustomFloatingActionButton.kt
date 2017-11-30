package com.graphicus.graphicus.gui.views

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.view.animation.ScaleAnimation
import com.gordonwong.materialsheetfab.AnimatedFab
import com.graphicus.graphicus.R


class CustomFloatingActionButton: FloatingActionButton, AnimatedFab  {

    private val FAB_ANIM_DURATION = 200

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun show() {
        show(0f, 0f)
    }

    override fun show(translationX: Float, translationY: Float) {
        // Set FAB's translation
        setTranslation(translationX, translationY)

        // Only use scale animation if FAB is hidden
        if (visibility != View.VISIBLE) {
            // Pivots indicate where the animation begins from
            val pivotX = pivotX + translationX
            val pivotY = pivotY + translationY

            val anim: ScaleAnimation
            // If pivots are 0, that means the FAB hasn't been drawn yet so just use the
            // center of the FAB
            anim = if (pivotX == 0f || pivotY == 0f) {
                ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f)
            } else {
                ScaleAnimation(0f, 1f, 0f, 1f, pivotX, pivotY)
            }

            // Animate FAB expanding
            anim.duration = FAB_ANIM_DURATION.toLong()
            anim.interpolator = getInterpolator()
            startAnimation(anim)
        }

        visibility = View.VISIBLE
    }

    override fun hide() {
        // Only use scale animation if FAB is visible
        if (visibility == View.VISIBLE) {
            // Pivots indicate where the animation begins from
            val pivotX = pivotX + translationX
            val pivotY = pivotY + translationY

            // Animate FAB shrinking
            val anim = ScaleAnimation(1f, 0f, 1f, 0f, pivotX, pivotY)
            anim.duration = FAB_ANIM_DURATION.toLong()
            anim.interpolator = getInterpolator()
            startAnimation(anim)
        }

        visibility = View.INVISIBLE
    }

    private fun setTranslation(translationX: Float, translationY: Float) {
        animate().setInterpolator(getInterpolator()).setDuration(FAB_ANIM_DURATION.toLong())
                .translationX(translationX).translationY(translationY)
    }

    private fun getInterpolator(): Interpolator {
        return AnimationUtils.loadInterpolator(context, R.interpolator.msf_interpolator)
    }
}