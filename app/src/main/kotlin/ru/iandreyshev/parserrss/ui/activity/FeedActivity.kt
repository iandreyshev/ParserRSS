package ru.iandreyshev.parserrss.ui.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatDialogFragment

import ru.iandreyshev.parserrss.models.viewModels.ViewRss
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

class FeedActivity : BaseActivity(), IFeedView, IOnArticleClickListener, AddRssDialog.IListener {

    companion object {
        private val TAG: String = FeedActivity::class.java.name
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

    private val mInteractor
        get() = presenter.interactor
    private lateinit var mPagesAdapter: FeedPagesAdapter
    private var mMenu: Menu? = null
    private var mIsAddButtonEnabled = true
    private var mIsInfoButtonEnabled = true
    private var mIsDeleteButtonEnabled = true
    private var mOpenedDialog: MvpAppCompatDialogFragment? = null

    override fun insertRss(rss: ViewRss) {
        mPagesAdapter.insert(rss)
    }

    override fun removeRss(rssId: Long) {
        mPagesAdapter.remove(rssId)
    }

    override fun openRssPage(rssId: Long) {
        val pagePosition = mPagesAdapter.getPagePositionById(rssId)

        if (pagePosition >= 0) {
            pagerLayout.currentItem = pagePosition
        }
    }

    override fun openArticle(articleId: Long) {
        val intent = ArticleActivity.getIntent(this)
                .putExtra(ArticleActivity.ARTICLE_BOUND_KEY, articleId)

        startActivity(intent)
    }

    override fun openAddingRssDialog(url: String) {
        openDialog(AddRssDialog.newInstance(url))
    }

    override fun openRssInfoDialog(rss: ViewRss) = openDialog(RssInfoDialog.newInstance(rss))

    override fun openInternetPermissionDialog() = openDialog(InternetPermissionDialog())

    override fun startProgressBar(isStart: Boolean) = progressBar.setVisibility(isStart)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_feed_options, menu)
        menu.findItem(ADD_BUTTON).isEnabled = mIsAddButtonEnabled
        menu.findItem(INFO_BUTTON).isEnabled = mIsInfoButtonEnabled
        menu.findItem(DELETE_BUTTON).isEnabled = mIsDeleteButtonEnabled
        mMenu = menu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            ADD_BUTTON -> openAddingRssDialog()
            INFO_BUTTON -> mInteractor.onOpenRssInfo(mPagesAdapter.getRss(pagerLayout.currentItem))
            DELETE_BUTTON -> mInteractor.onDeleteRss(mPagesAdapter.getRss(pagerLayout.currentItem)?.id)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onArticleClick(articleId: Long) = mInteractor.onOpenArticle(articleId)

    override fun onAddRss(url: String) {
        mInteractor.onAddNewRss(url)
    }

    override fun enableAddButton(isEnabled: Boolean) {
        mIsAddButtonEnabled = isEnabled
        mMenu?.findItem(ADD_BUTTON)?.isEnabled = isEnabled
    }

    override fun enableInfoButton(isEnabled: Boolean) {
        mIsInfoButtonEnabled = isEnabled
        mMenu?.findItem(INFO_BUTTON)?.isEnabled = isEnabled
    }

    override fun enableDeleteButton(isEnabled: Boolean) {
        mIsDeleteButtonEnabled = isEnabled
        mMenu?.findItem(DELETE_BUTTON)?.isEnabled = isEnabled
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
        intContentView()
    }

    override fun setToolbarScrollable(isScrollable: Boolean) {
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (isScrollable) TOOLBAR_SCROLL_ON else TOOLBAR_SCROLL_OFF
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        startProgressBar(false)
        setToolbarScrollable(false)
    }

    private fun initTabsView() {
        mPagesAdapter = FeedPagesAdapter(supportFragmentManager)
        pagerLayout.adapter = mPagesAdapter
        tabsLayout.setupWithViewPager(pagerLayout)
    }

    private fun intContentView() {
        insertButton.setOnClickListener { openAddingRssDialog() }
    }

    private fun openDialog(dialog: MvpAppCompatDialogFragment) {
        mOpenedDialog?.dismiss()
        mOpenedDialog = dialog
        mOpenedDialog?.show(supportFragmentManager ?: return, TAG)
    }
}
