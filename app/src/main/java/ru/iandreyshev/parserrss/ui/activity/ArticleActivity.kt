package ru.iandreyshev.parserrss.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.ImagesLoadPresenter
import ru.iandreyshev.parserrss.presentation.view.IArticleView
import ru.iandreyshev.parserrss.presentation.presenter.ArticlePresenter

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.presentation.view.IImageView

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter

import java.text.SimpleDateFormat
import java.util.Locale

import kotlinx.android.synthetic.main.activity_article.*

class ArticleActivity : BaseActivity(),
        IArticleView,
        IImageView {
    companion object {
        const val ARTICLE_BOUND_KEY = "Article_to_open"
        private const val DEFAULT_ARTICLE_ID: Long = 0
        private const val BACK_BUTTON = android.R.id.home
        private const val OPEN_IN_BROWSER_BUTTON = R.id.article_option_open_in_browser
        private val DATE_FORMAT = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)

        fun getIntent(context: Context): Intent {
            return Intent(context, ArticleActivity::class.java)
        }
    }

    @InjectPresenter
    lateinit var presenter: ArticlePresenter
    @InjectPresenter(type = PresenterType.GLOBAL, tag = ImagesLoadPresenter.TAG)
    lateinit var imageLoadPresenter: ImagesLoadPresenter

    @ProvidePresenter
    fun provideArticlePresenter(): ArticlePresenter {
        return ArticlePresenter(intent.getLongExtra(ARTICLE_BOUND_KEY, DEFAULT_ARTICLE_ID))
    }

    override fun closeArticle() {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_article_options, menu)

        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            BACK_BUTTON -> closeArticle()
            OPEN_IN_BROWSER_BUTTON -> presenter.onOpenOriginal()
        }

        return super.onOptionsItemSelected(menuItem)
    }

    override fun initArticle(rss: ViewRss, article: ViewArticle) {
        supportActionBar?.title = rss.title
        titleView.text = article.title
        descriptionView.text = article.description
        imageLoadPresenter.loadImage(article.id)
        loadDate(article.date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        initToolbar()
        initImageView()
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
        dateView.visibility = if (date == null) View.GONE else View.VISIBLE
        dateView.text = DATE_FORMAT.format(date)
    }

    override fun insertImage(bitmap: Bitmap) {
        imageView.visibility = View.VISIBLE
        imageView.setImageBitmap(bitmap)
    }
}
