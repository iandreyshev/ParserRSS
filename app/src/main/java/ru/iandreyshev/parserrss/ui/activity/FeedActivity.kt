package ru.iandreyshev.parserrss.ui.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.Menu
import android.view.MenuItem

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IFeedView
import ru.iandreyshev.parserrss.presentation.presenter.FeedPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.ui.adapter.FeedPagesAdapter
import ru.iandreyshev.parserrss.ui.fragment.AddRssDialog
import ru.iandreyshev.parserrss.ui.fragment.RssInfoDialog
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener

import kotlinx.android.synthetic.main.activity_feed.*

import com.arellomobile.mvp.presenter.InjectPresenter
import ru.iandreyshev.parserrss.ui.extention.setVisibility
import ru.iandreyshev.parserrss.ui.fragment.InternetPermissionDialog

class FeedActivity : BaseActivity(),
        IFeedView,
        IOnArticleClickListener,
        AddRssDialog.IOnSubmitListener,
        InternetPermissionDialog.IOnOpenSettingsListener {

    companion object {
        private const val ADD_BUTTON = R.id.feed_options_add
        private const val INFO_BUTTON = R.id.feed_options_info
        private const val DELETE_BUTTON = R.id.feed_options_delete

        private const val TOOLBAR_SCROLL_OFF = 0
        private const val TOOLBAR_SCROLL_ON =
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
    }

    @InjectPresenter
    lateinit var presenter: FeedPresenter

    private val _interactor
        get() = presenter.interactor
    private lateinit var _pagesAdapter: FeedPagesAdapter
    private var _menu: Menu? = null
    private var _isAddButtonEnabled = true
    private var _isInfoButtonEnabled = true
    private var _isDeleteButtonEnabled = true

    override fun insertRss(rss: ViewRss) = _pagesAdapter.insert(rss)

    override fun removeRss(rss: ViewRss) = _pagesAdapter.remove(rss)

    override fun openPage(position: Int) {
        if (!_pagesAdapter.isEmpty || position in 0 until _pagesAdapter.count) {
            pagerLayout.currentItem = position
        }
    }

    override fun openArticle(articleId: Long) {
        val intent = ArticleActivity.getIntent(this)
                .putExtra(ArticleActivity.ARTICLE_BOUND_KEY, articleId)

        startActivity(intent)
    }

    override fun openAddingRssDialog() = AddRssDialog.show(supportFragmentManager)

    override fun openRssInfo(rss: ViewRss) = RssInfoDialog.show(supportFragmentManager, rss)

    override fun startProgressBar(isStart: Boolean) = progressBar.setVisibility(isStart)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_feed_options, menu)

        this._menu = menu
        menu.findItem(ADD_BUTTON).isEnabled = _isAddButtonEnabled
        menu.findItem(INFO_BUTTON).isEnabled = _isInfoButtonEnabled
        menu.findItem(DELETE_BUTTON).isEnabled = _isDeleteButtonEnabled

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            ADD_BUTTON -> openAddingRssDialog()
            INFO_BUTTON -> _interactor.onOpenRssInfo(_pagesAdapter.getRss(pagerLayout.currentItem))
            DELETE_BUTTON -> _interactor.onDeleteRss(_pagesAdapter.getRss(pagerLayout.currentItem))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onArticleClick(articleId: Long) = _interactor.onOpenArticle(articleId)

    override fun addRss(url: String) = _interactor.onInsertRss(url)

    override fun enableAddButton(isEnabled: Boolean) {
        _isAddButtonEnabled = isEnabled
        _menu?.findItem(ADD_BUTTON)?.isEnabled = isEnabled
    }

    override fun enableInfoButton(isEnabled: Boolean) {
        _isInfoButtonEnabled = isEnabled
        _menu?.findItem(INFO_BUTTON)?.isEnabled = isEnabled
    }

    override fun enableDeleteButton(isEnabled: Boolean) {
        _isDeleteButtonEnabled = isEnabled
        _menu?.findItem(DELETE_BUTTON)?.isEnabled = isEnabled
    }

    override fun openToolbarTitle(isOpen: Boolean) {
        tabsLayout.setVisibility(!isOpen)
        titleView.setVisibility(isOpen)
        titleView.text = getString(R.string.feed_toolbar_title)
    }

    override fun openEmptyContentMessage(isOpen: Boolean) {
        pagerLayout.setVisibility(!isOpen)
        tabsLayout.setVisibility(!isOpen)
        contentMessageLayout.setVisibility(isOpen)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        initToolbar()
        initTabsView()
    }

    override fun setToolbarScrollable(isScrollable: Boolean) {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (isScrollable) TOOLBAR_SCROLL_ON else TOOLBAR_SCROLL_OFF
    }

    override fun openInternetPermissionDialog() {}

    override fun openSettings() {}

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        startProgressBar(false)
        setToolbarScrollable(false)
    }

    private fun initTabsView() {
        _pagesAdapter = FeedPagesAdapter(supportFragmentManager)
        pagerLayout.adapter = _pagesAdapter
        tabsLayout.setupWithViewPager(pagerLayout)
    }
}
