package ru.iandreyshev.parserrss.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.FeedPagePresenter
import ru.iandreyshev.parserrss.presentation.presenter.ImagesLoadPresenter
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView
import ru.iandreyshev.parserrss.presentation.view.IImageView
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener

import kotlinx.android.synthetic.main.view_feed_list.*

class FeedPageFragment : BaseFragment(),
        IFeedPageView,
        IImageView {

    companion object {
        private const val MAX_SCROLL_SPEED_TO_UPDATE_IMAGES = 15

        fun newInstance(rss: ViewRss): FeedPageFragment {
            val fragment = FeedPageFragment()
            fragment.presenter = FeedPagePresenter(rss)

            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: FeedPagePresenter
    @InjectPresenter(type = PresenterType.GLOBAL, tag = ImagesLoadPresenter.TAG)
    lateinit var imageLoader: ImagesLoadPresenter

    private val listAdapter: FeedListAdapter = FeedListAdapter()

    @ProvidePresenter
    fun provideFeedPagePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstantState: Bundle?): View? {
        return inflater.inflate(R.layout.view_feed_list, viewGroup, false)
    }

    override fun onResume() {
        super.onResume()
        updateImages(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListView()
        refreshLayout.setOnRefreshListener({ presenter.onUpdate() })
    }

    override fun startUpdate(isStart: Boolean) {
        refreshLayout.isRefreshing = isStart
    }

    override fun setArticles(newArticles: List<ViewArticle>) {
        listAdapter.setArticles(newArticles)
    }

    override fun updateImages(isWithoutQueue: Boolean) {
        listAdapter.forEach { imageLoader.getIconForItem(it, isWithoutQueue) }
    }

    override fun insertImage(bitmap: Bitmap) {}

    private fun initListView() {
        itemsList.adapter = listAdapter
        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.addOnScrollListener(ScrollListener())
        itemsList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        listAdapter.setArticleClickListener(context as IOnArticleClickListener)
    }

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (Math.abs(dy) <= MAX_SCROLL_SPEED_TO_UPDATE_IMAGES) {
                updateImages(false)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            updateImages(false)
        }
    }
}