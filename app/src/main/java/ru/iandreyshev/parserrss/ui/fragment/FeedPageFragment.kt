package ru.iandreyshev.parserrss.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.FeedPagePresenter
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener

import kotlinx.android.synthetic.main.view_feed_list.*
import ru.iandreyshev.parserrss.presentation.view.IItemsListView
import ru.iandreyshev.parserrss.ui.extention.setVisibility
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

    private val _interactor
        get() = presenter.interactor
    private val _listAdapter: FeedListAdapter = FeedListAdapter()

    @ProvidePresenter
    fun provideFeedPagePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstantState: Bundle?): View? {
        return inflater.inflate(R.layout.view_feed_list, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListView()
        refreshLayout.setOnRefreshListener({
            _interactor.onUpdate()
        })
    }

    override fun openEmptyContentMessage(isOpen: Boolean) {
        itemsList.setVisibility(!isOpen)
        contentMessageLayout.setVisibility(isOpen)
    }

    override fun startUpdate(isStart: Boolean) {
        refreshLayout.isRefreshing = isStart
    }

    override fun setArticles(newArticles: List<ViewArticle>) {
        _listAdapter.setArticles(newArticles)
        itemsList.adapter = null
        itemsList.adapter = _listAdapter
    }

    override fun updateImages() {
        _listAdapter.forEach { _interactor.load(it) }
    }

    override fun openInternetPermissionDialog() {
        InternetPermissionDialog.show(fragmentManager ?: return)
    }

    private fun initListView() {
        itemsList.adapter = _listAdapter
        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.addOnScrollListener(ScrollListener(this))
        itemsList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        _listAdapter.setArticleClickListener(context as IOnArticleClickListener)
    }

    private class ScrollListener(fragment: FeedPageFragment) : RecyclerView.OnScrollListener() {
        private val _fragment = WeakReference(fragment)

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (Math.abs(dy) <= MAX_SCROLL_SPEED_TO_UPDATE_IMAGES) {
                _fragment.get()?.updateImages()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            _fragment.get()?.updateImages()
        }
    }
}
