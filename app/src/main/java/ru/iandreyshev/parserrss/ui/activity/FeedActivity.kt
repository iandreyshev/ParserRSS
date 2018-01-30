package ru.iandreyshev.parserrss.ui.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.Menu
import android.view.MenuItem
import android.view.View

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IFeedView
import ru.iandreyshev.parserrss.presentation.presenter.FeedPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.ui.adapter.FeedPagesAdapter
import ru.iandreyshev.parserrss.ui.fragment.AddRssDialog
import ru.iandreyshev.parserrss.ui.fragment.RssInfoDialog
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener
import ru.iandreyshev.parserrss.ui.listeners.IOnSubmitAddRssListener

import kotlinx.android.synthetic.main.activity_feed.*

import com.arellomobile.mvp.presenter.InjectPresenter

class FeedActivity : BaseActivity(),
        IFeedView,
        IOnArticleClickListener,
        IOnSubmitAddRssListener {

    companion object {
        private const val ADD_BUTTON = R.id.feed_options_add
        private const val INFO_BUTTON = R.id.feed_options_info
        private const val DELETE_BUTTON = R.id.feed_options_delete

        private const val TOOLBAR_SCROLL_ON =
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        private const val TOOLBAR_SCROLL_OFF = 0
    }

    @InjectPresenter
    lateinit var presenter: FeedPresenter

    private lateinit var pagesAdapter: FeedPagesAdapter
    private lateinit var menuInfoButton: MenuItem
    private lateinit var menuDeleteButton: MenuItem

    override fun insertRss(rss: ViewRss) {
        pagesAdapter.insert(rss)
        onFeedUpdate()
    }

    override fun removeRss(rss: ViewRss) {
        pagesAdapter.remove(rss)
        pagerLayout.adapter = pagesAdapter
        onFeedUpdate()
    }

    override fun openPage(position: Int) {
        if (!pagesAdapter.isEmpty || position in 0 until pagesAdapter.count) {
            pagerLayout.currentItem = position
        }
    }

    override fun openArticle(articleId: Long) {
        val intent = ArticleActivity.getIntent(this)
                .putExtra(ArticleActivity.ARTICLE_BOUND_KEY, articleId)

        startActivity(intent)
    }

    override fun openAddingRssDialog() {
        AddRssDialog.show(supportFragmentManager)
    }

    override fun startProgressBar(isStart: Boolean) {
        progressBar.visibility = if (isStart) View.VISIBLE else View.GONE
    }

    override fun openRssInfo(rss: ViewRss) {
        RssInfoDialog.show(supportFragmentManager, rss)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_feed_options, menu)
        menuInfoButton = menu.findItem(INFO_BUTTON)
        menuDeleteButton = menu.findItem(DELETE_BUTTON)

        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        menuInfoButton.isEnabled = !pagesAdapter.isEmpty
        menuDeleteButton.isEnabled = !pagesAdapter.isEmpty

        return super.onMenuOpened(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            ADD_BUTTON -> openAddingRssDialog()
            INFO_BUTTON -> presenter.openRssInfo(pagesAdapter.getRss(pagerLayout.currentItem))
            DELETE_BUTTON -> presenter.onDeleteRss(pagesAdapter.getRss(pagerLayout.currentItem))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onArticleClick(articleId: Long) {
        presenter.openArticle(articleId)
    }

    override fun onSubmitAddRss(url: String) {
        presenter.onInsertRss(url)
    }

    override fun onFeedUpdate() {
        contentMessage.visibility = if (pagesAdapter.isEmpty) View.VISIBLE else View.GONE
        pagerLayout.visibility = if (pagesAdapter.isEmpty) View.GONE else View.VISIBLE
        tabsLayout.visibility = if (pagesAdapter.isEmpty) View.GONE else View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        initToolbar()
        initTabsView()
        onFeedUpdate()
    }

    override fun setToolbarScrollable(isScrollable: Boolean) {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (isScrollable) TOOLBAR_SCROLL_ON else TOOLBAR_SCROLL_OFF
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        startProgressBar(false)
    }

    private fun initTabsView() {
        pagesAdapter = FeedPagesAdapter(supportFragmentManager)
        pagerLayout.adapter = pagesAdapter
        tabsLayout.setupWithViewPager(pagerLayout)
    }
}
