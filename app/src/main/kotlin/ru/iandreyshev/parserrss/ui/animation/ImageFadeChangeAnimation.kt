package ru.iandreyshev.parserrss.ui.animation

import android.content.Context
import android.graphics.Bitmap
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class ImageFadeChangeAnimation(context: Context, inDurationMs: Long, outDurationMs: Long) : IImageAnimation {

    private val mAnimOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    private val mAnimIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)

    init {
        mAnimOut.duration = outDurationMs
        mAnimIn.duration = inDurationMs
    }

    override fun start(view: ImageView, imageBitmap: Bitmap) {
        mAnimOut.setAnimationListener(object : EmptyAnimationListener() {
            override fun onAnimationEnd(animation: Animation) {
                view.setImageBitmap(imageBitmap)
                view.startAnimation(mAnimIn)
            }
        })

        view.startAnimation(mAnimOut)
    }
}
