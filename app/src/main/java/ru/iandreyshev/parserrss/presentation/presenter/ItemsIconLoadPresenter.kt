package ru.iandreyshev.parserrss.presentation.presenter

import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.interactor.ItemIconLoadInteractor
import ru.iandreyshev.parserrss.presentation.view.IItemsListView
import ru.iandreyshev.parserrss.ui.adapter.IFeedItem

@InjectViewState
class ItemsIconLoadPresenter : MvpPresenter<IItemsListView>() {
    companion object {
        const val TAG = "ItemsIconLoadPresenter"
    }

    val interactor = ItemIconLoadInteractor(ItemIconLoadInteractorOutputPort())

    private inner class ItemIconLoadInteractorOutputPort : ItemIconLoadInteractor.IOutputPort {
        override fun insertImage(item: IFeedItem, imageBitmap: Bitmap) {
            item.updateImage(imageBitmap)
        }
    }
}
