package com.example.todoapp.ui.util

import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.marginEnd
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * Class for View Animations.
 */
object ViewAnimations {

    // Animate height of View
    fun animateNewHeight(view: View, newHeight: Int) {
        val valueAnimator = ValueAnimator.ofInt(view.measuredHeight, newHeight)
        valueAnimator.duration = 500L
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }
        valueAnimator.start()
    }

    // Animate marginEnd of View
    fun animateMarginEnd(view: View, newMarginEnd: Int, duration: Long) {
        val valueAnimator = ValueAnimator.ofInt(view.marginEnd, newMarginEnd)
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams as CollapsingToolbarLayout.LayoutParams
            layoutParams.marginEnd = animatedValue
            view.layoutParams = layoutParams
        }
        valueAnimator.start()
    }

    // Animate alpha of View
    fun animateAlpha(view: View, newAlpha: Float, duration: Long){
        val valueAnimator = ValueAnimator.ofFloat(view.alpha, newAlpha)
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener {
            val animatedValue = valueAnimator.animatedValue as Float
            view.alpha = animatedValue
        }
        valueAnimator.start()
    }
}