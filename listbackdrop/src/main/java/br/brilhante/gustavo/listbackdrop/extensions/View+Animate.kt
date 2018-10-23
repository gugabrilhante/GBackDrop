package br.brilhante.gustavo.listbackdrop.extensions

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Interpolator

fun View.animateAlpha(alpha: Float, time: Long = 0): ViewPropertyAnimator {
    return this.animate().alpha(alpha).setDuration(time)
}

fun View.animateY(y: Float, time: Long = 0): ViewPropertyAnimator {
    return this.animate().y(y).setDuration(time)
}

fun View.animateX(x: Float, time: Long = 0): ViewPropertyAnimator {
    return this.animate().x(x).setDuration(time)
}

fun View.createObjectAnimatorTranslationY(y: Float, duration: Long, interpolator: Interpolator): ObjectAnimator {
    val animator = ObjectAnimator.ofFloat(this, "translationY", y)
    animator.duration = duration
    interpolator.let { interpolator ->
        animator.interpolator = interpolator
    }
    return animator
}

fun View.createObjectAnimatorAlpha(alpha: Float, duration: Long, interpolator: Interpolator): ObjectAnimator {
    val animator = ObjectAnimator.ofFloat(this, "alpha", alpha)
    animator.duration = duration
    interpolator.let { interpolator ->
        animator.interpolator = interpolator
    }
    return animator
}