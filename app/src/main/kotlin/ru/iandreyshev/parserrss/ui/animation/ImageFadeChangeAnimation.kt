package ru.iandreyshev.parserrss.ui.animation

import android.content.Context
import android.graphics.Bitmap
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class ImageFadeChangeAnimation(context: Context, inDurationMs: Long, outDurationMs: Long) {

    private val mAnimOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    private val mAnimIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)

    init {
        mAnimOut.duration = outDurationMs
        mAnimIn.duration = inDurationMs
    }

    fun start(view: ImageView, bitmap: Bitmap) {
        mAnimOut.setAnimationListener(object : EmptyAnimationListener() {
            override fun onAnimationEnd(animation: Animation) {
                view.setImageBitmap(bitmap)
                view.startAnimation(mAnimIn)
            }
        })

        view.startAnimation(mAnimOut)
    }
}
