package ru.iandreyshev.parserrss.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.Rss
import ru.iandreyshev.parserrss.presentation.view.IArticleView
import ru.iandreyshev.parserrss.presentation.presenter.ArticlePresenter

import ru.iandreyshev.parserrss.R

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

import kotlinx.android.synthetic.main.activity_article.*
import ru.iandreyshev.parserrss.ui.animation.ImageFadeChangeAnimation
import ru.iandreyshev.parserrss.ui.extention.dateString
import ru.iandreyshev.parserrss.ui.extention.setVisibility

class ArticleActivity : BaseActivity(), IArticleView {

    companion object {
        const val ARTICLE_BOUND_KEY = "Article_to_open"
        private const val DEFAULT_ARTICLE_ID: Long = 0
        private const val MENU_ENABLED_ALPHA = 255
        private const val MENU_DISABLED_ALPHA = 96
        private const val BACK_BUTTON = android.R.id.home
        private const val OPEN_IN_BROWSER_BUTTON = R.id.article_option_open_in_browser
        private const val IMAGE_IN_DURATION_MS: Long = 750
        private const val IMAGE_OUT_DURATION_MS: Long = 0

        fun getIntent(context: Context): Intent {
            return Intent(context, ArticleActivity::class.java)
        }
    }

    @InjectPresenter
    lateinit var mPresenter: ArticlePresenter

    private val mInteractor
        get() = mPresenter.interactor
    private var mIsOpenOriginalEnabled = false

    @ProvidePresenter
    fun provideArticlePresenter(): ArticlePresenter {
        val articleId = intent.getLongExtra(ARTICLE_BOUND_KEY, DEFAULT_ARTICLE_ID)

        return ArticlePresenter(articleId)
    }

    override fun closeArticle() = finish()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_options, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.findItem(OPEN_IN_BROWSER_BUTTON)
        item?.isEnabled = mIsOpenOriginalEnabled
        item?.icon?.alpha = if (mIsOpenOriginalEnabled) MENU_ENABLED_ALPHA else MENU_DISABLED_ALPHA

        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            BACK_BUTTON -> closeArticle()
            OPEN_IN_BROWSER_BUTTON -> mInteractor.onOpenOriginal()
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun initArticle(rss: Rss, article: Article) {
        toolbarTitle.text = rss.title
        titleView.text = article.title
        descriptionView.text = article.description
        loadDate(article.date)
        mIsOpenOriginalEnabled = article.originUrl != null
        invalidateOptionsMenu()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        initToolbar()
        initImageView()
    }

    override fun setImage(imageBitmap: Bitmap) {
        imageView.visibility = View.VISIBLE
        imageView.setImageBitmap(imageBitmap)
        ImageFadeChangeAnimation(this, IMAGE_IN_DURATION_MS, IMAGE_OUT_DURATION_MS)
                .start(imageView, imageBitmap)
    }

    override fun startProgressBar(isStart: Boolean) {
        progressBar.setVisibility(isStart)
        toolbarContent.invalidate()
        toolbarContent.requestLayout()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)

        if (supportActionBar == null) {
            return
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initImageView() {
        imageView.visibility = View.GONE
    }

    private fun loadDate(date: Long?) {
        dateView.setVisibility(date != null)
        dateView.text = date?.dateString
    }
}
