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
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener

import kotlinx.android.synthetic.main.view_feed_list.*
import ru.iandreyshev.parserrss.presentation.presenter.ItemsIconLoadPresenter
import ru.iandreyshev.parserrss.presentation.view.IItemsListView
import java.lang.ref.WeakReference

class FeedPageFragment : BaseFragment(),
        IFeedPageView,
        IItemsListView {

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
    @InjectPresenter(type = PresenterType.GLOBAL, tag = ItemsIconLoadPresenter.TAG)
    lateinit var iconsLoadPresenter: ItemsIconLoadPresenter

    private val interactor
        get() = presenter.interactor
    private val itemsIconsInteractor
        get() = iconsLoadPresenter.interactor
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
        refreshLayout.setOnRefreshListener({ interactor.onUpdate() })
    }

    override fun openMessage(isOpen: Boolean, message: String) {
        itemsList.visibility = if (isOpen) View.GONE else View.VISIBLE
        contentMessageView.visibility = if (isOpen) View.VISIBLE else View.GONE
        contentMessageView.text = message
    }

    override fun startUpdate(isStart: Boolean) {
        refreshLayout.isRefreshing = isStart
    }

    override fun setArticles(newArticles: List<ViewArticle>) {
        listAdapter.setArticles(newArticles)
    }

    override fun updateImages(isWithoutQueue: Boolean) {
        listAdapter.forEach { itemsIconsInteractor.getIconForItem(it, isWithoutQueue) }
    }

    private fun initListView() {
        itemsList.adapter = listAdapter
        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.addOnScrollListener(ScrollListener(this))
        itemsList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        listAdapter.setArticleClickListener(context as IOnArticleClickListener)
    }

    class ScrollListener(fragment: FeedPageFragment) : RecyclerView.OnScrollListener() {
        private val fragment = WeakReference(fragment)

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (Math.abs(dy) <= MAX_SCROLL_SPEED_TO_UPDATE_IMAGES) {
                fragment.get()?.updateImages(false)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            fragment.get()?.updateImages(false)
        }
    }
}
