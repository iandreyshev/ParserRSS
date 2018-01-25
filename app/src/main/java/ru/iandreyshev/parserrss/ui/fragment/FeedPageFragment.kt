package ru.iandreyshev.parserrss.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

import kotlinx.android.synthetic.main.feed_list.*

class FeedPageFragment : BaseFragment(), IFeedPageView, IImageView {
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

    private var listAdapter: FeedListAdapter = FeedListAdapter()

    @ProvidePresenter
    fun provideFeedPagePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstantState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_list, viewGroup, false)
    }

    override fun onStart() {
        super.onStart()

        initListView()
        refreshLayout.setOnRefreshListener({ presenter.onUpdate() })
        updateImages(true)
    }

    override fun startUpdate(isStart: Boolean) {
        refreshLayout.isRefreshing = isStart
    }

    override fun setArticles(newArticles: List<ViewArticle>) {
        listAdapter.setArticles(newArticles)
        updateImages(true)
    }

    override fun updateImages(isWithoutQueue: Boolean) {
        for (item in listAdapter.itemsOnWindow) {
            imageLoader.getIconForItem(item, isWithoutQueue)
        }
    }

    private fun initListView() {
        listAdapter.setArticleClickListener(context as IOnArticleClickListener)

        itemsList.adapter = listAdapter
        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.addOnScrollListener(ScrollListener(this))
        itemsList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    class ScrollListener(private val fragment: FeedPageFragment) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (Math.abs(dy) <= MAX_SCROLL_SPEED_TO_UPDATE_IMAGES) {
                fragment.updateImages(false)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            fragment.updateImages(false)
        }
    }
}
