package ru.iandreyshev.parserrss.presentation.presenter

import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.interactor.ImagesLoadInteractor

import ru.iandreyshev.parserrss.presentation.view.IImageView

@InjectViewState
class ImagesLoadPresenter : MvpPresenter<IImageView>() {
    val interactor = ImagesLoadInteractor(LoadImageInteractorOutputPort())

    private inner class LoadImageInteractorOutputPort : ImagesLoadInteractor.IOutputPort {
        override fun insertImage(imageBitmap: Bitmap) = viewState.insertImage(imageBitmap)
    }
}
