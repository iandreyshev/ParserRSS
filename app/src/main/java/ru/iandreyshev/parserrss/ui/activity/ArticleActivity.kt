package ru.iandreyshev.parserrss.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.support.v7.widget.Toolbar

import butterknife.BindView
import butterknife.ButterKnife
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

class ArticleActivity : BaseActivity(), IArticleView, IImageView {

    @InjectPresenter
    internal var mArticlePresenter: ArticlePresenter? = null
    @InjectPresenter(type = PresenterType.GLOBAL, tag = ImagesLoadPresenter.TAG)
    internal var mImageLoadPresenter: ImagesLoadPresenter? = null

    @BindView(R.id.article_title)
    internal var mTitle: TextView? = null
    @BindView(R.id.article_text)
    internal var mText: TextView? = null
    @BindView(R.id.article_date)
    internal var mDate: TextView? = null
    @BindView(R.id.article_image)
    internal var mImage: ImageView? = null
    @BindView(R.id.article_toolbar)
    internal var mToolbar: Toolbar? = null

    private val mArticleId: Long
    private var mArticle: ViewArticle? = null

    @ProvidePresenter
    fun provideArticlePresenter() = ArticlePresenter(mArticleId!!)

    override fun closeArticle() {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.article_options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> closeArticle()
            R.id.article_option_open_in_browser -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mArticle!!.originUrl)))
        }

        return super.onOptionsItemSelected(menuItem)
    }

    override fun initArticle(rss: ViewRss, article: ViewArticle) {
        mArticle = article
        mTitle!!.text = article.title
        mText!!.text = article.description
        mImageLoadPresenter!!.loadImage(mArticle!!.id)
        loadDate(article.date)

        if (supportActionBar != null) {
            supportActionBar!!.title = rss.title
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
        setContentView(R.layout.activity_article)

        ButterKnife.bind(this)

        initToolbar()
        initArticle()
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)

        if (supportActionBar == null) {
            return
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    private fun initArticle() {
        mArticleId = intent.getLongExtra(ARTICLE_BOUND_KEY, DEFAULT_ARTICLE_ID)
    }

    private fun setViewVisible(view: View?, isVisible: Boolean) {
        view!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun loadDate(date: Long?) {
        setViewVisible(mDate, date != null)
        mDate!!.text = DATE_FORMAT.format(date)
    }

    override fun insertImage(bitmap: Bitmap) {
        mImage!!.visibility = View.VISIBLE
        mImage!!.setImageBitmap(bitmap)
    }

    companion object {
        val ARTICLE_BOUND_KEY = "Article_to_open"
        private val DEFAULT_ARTICLE_ID: Long = 0
        private val DATE_FORMAT = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)

        fun getIntent(context: Context): Intent {
            return Intent(context, ArticleActivity::class.java)
        }
    }
}
