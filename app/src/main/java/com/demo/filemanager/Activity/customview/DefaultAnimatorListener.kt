package com.demo.filemanager.Activity.customview

import android.animation.Animator

internal open class DefaultAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationCancel(animator: Animator) {}
    override fun onAnimationEnd(animator: Animator) {}
    override fun onAnimationEnd(animator: Animator, z: Boolean) {}
    override fun onAnimationRepeat(animator: Animator) {}
    override fun onAnimationStart(animator: Animator) {}
    override fun onAnimationStart(animator: Animator, z: Boolean) {}
}
