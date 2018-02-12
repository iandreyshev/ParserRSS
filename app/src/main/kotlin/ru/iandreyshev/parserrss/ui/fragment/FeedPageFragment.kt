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
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.presentation.presenter.FeedPagePresenter
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView
import ru.iandreyshev.parserrss.ui.adapter.FeedListAdapter
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener

import kotlinx.android.synthetic.main.view_feed_list.*
import ru.iandreyshev.parserrss.ui.extention.setVisibility
import java.lang.ref.WeakReference

class FeedPageFragment : BaseFragment(), IFeedPageView {

    companion object {
        private val TAG = FeedPagePresenter::class.java.name
        private const val MAX_SCROLL_SPEED_TO_UPDATE_IMAGES = 50

        fun newInstance(rssId: Long): FeedPageFragment {
            val fragment = FeedPageFragment()
            fragment.mPresenter = FeedPagePresenter(rssId)

            return fragment
        }
    }

    @InjectPresenter
    lateinit var mPresenter: FeedPagePresenter

    private val mInteractor
        get() = mPresenter.interactor
    private val mListAdapter: FeedListAdapter = FeedListAdapter()

    @ProvidePresenter
    fun provideFeedPagePresenter() = mPresenter

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstantState: Bundle?): View? {
        return inflater.inflate(R.layout.view_feed_list, viewGroup, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListView()
        refreshLayout.setOnRefreshListener({
            mInteractor.onUpdate()
        })
    }

    override fun onResume() {
        super.onResume()
        mListAdapter.itemsOnWindowForEach {
            if (!it.isUpdateEnd) {
                it.resetUpdate()
                it.onStartUpdate()
                mInteractor.updateImage(it)
            }
        }
    }

    override fun openEmptyContentMessage(isOpen: Boolean) {
        itemsList.setVisibility(!isOpen)
        contentMessageLayout.setVisibility(isOpen)
    }

    override fun startUpdate(isStart: Boolean) {
        refreshLayout.isRefreshing = isStart
    }

    override fun setArticles(newArticles: MutableList<Article>) {
        mListAdapter.setArticles(newArticles)
        itemsList.adapter = null
        itemsList.adapter = mListAdapter
    }

    override fun updateImages() {
        mListAdapter.itemsOnWindowForEach {
            if (!it.isUpdateStart) {
                it.onStartUpdate()
                mInteractor.updateImage(it)
            }
        }
    }

    override fun openInternetPermissionDialog() {
        InternetPermissionDialog().show(fragmentManager, TAG)
    }

    private fun initListView() {
        itemsList.adapter = mListAdapter
        itemsList.layoutManager = LinearLayoutManager(context)
        itemsList.addOnScrollListener(ScrollListener(this))
        itemsList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        mListAdapter.setArticleClickListener(context as IOnArticleClickListener)
    }

    private class ScrollListener(fragment: FeedPageFragment) : RecyclerView.OnScrollListener() {
        private val mFragment = WeakReference(fragment)

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (Math.abs(dy) <= MAX_SCROLL_SPEED_TO_UPDATE_IMAGES) {
                mFragment.get()?.updateImages()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            mFragment.get()?.updateImages()
        }
    }
}
