package uz.xabardor.ui.news

import android.content.Intent
import android.text.Html
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import moxy.presenter.InjectPresenter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import moxy.presenter.ProvidePresenter
import uz.xabardor.R
import uz.xabardor.extensions.*
import uz.xabardor.extensions.language.Krill
import uz.xabardor.extensions.language.Uzbek
import uz.xabardor.rest.models.Adsense
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.BaseService
import uz.xabardor.ui.base.BaseActivity
import uz.xabardor.ui.base.recyclerview.OnItemClickListener


class NewsActivity : BaseActivity(), NewsView, OnItemClickListener<News>, TagView.OnTagClickListener {

    @InjectPresenter
    lateinit var presenter: NewsPresenter

    @ProvidePresenter
    fun providerPresenter() = NewsPresenter().apply {
        news = this@NewsActivity.news
    }

    override val layoutId: Int
        get() = R.layout.activity_news


    lateinit var relatedRecyclerView: RecyclerView
    lateinit var relatedAdapter: RelatedAdapter

//    lateinit var webView: WebView

    lateinit var tagContainerLayout: TagContainerLayout


    lateinit var titleTextView: TextView
    lateinit var messageTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var countViewTextView: TextView

    lateinit var textViewAdsense : TextView
    lateinit var imageViewAdsense : ImageView
    lateinit var adsenseLay : RelativeLayout


    lateinit var authorLinearLayout: LinearLayout
    lateinit var authorNameTextView: TextView
    lateinit var authorDescriptionTextView: TextView
    lateinit var authorImageView: CircleImageView

    lateinit var progressBar: ProgressBar
    lateinit var scrollView: NestedScrollView


    lateinit var imageLinearLayout: LinearLayout
    lateinit var imageView: ImageView
    lateinit var imageCaptionTextView: TextView

    override fun setupToolbar() {
        toolBarLogoImageView?.visibility = View.VISIBLE
    }

    override fun onCreatedView() {
        progressBar = findViewById(R.id.progress_bar)
        scrollView = findViewById(R.id.scroll_view)
        textViewAdsense = findViewById(R.id.adsense_text_view_title)
        imageViewAdsense = findViewById(R.id.adsense_image_view)
        adsenseLay = findViewById(R.id.adsense_lay)

        relatedRecyclerView = findViewById(R.id.recyclerview)
        relatedAdapter = RelatedAdapter(relatedRecyclerView)
        relatedAdapter.language = languageManager.currentLanguage
        relatedAdapter.onItemClickListener = this


//        webView = findViewById(R.id.web_view)
//        webView.settings.javaScriptEnabled = true
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//            }
//
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                url?.let {
//                    openBrowser(it)
//                }
//                return true
//            }
//        }


        authorLinearLayout = findViewById(R.id.linear_layout_author)
        authorNameTextView = findViewById(R.id.text_view_author_name)
        authorDescriptionTextView = findViewById(R.id.text_view_author_description)
        authorImageView = findViewById(R.id.image_view_author)



        tagContainerLayout = findViewById(R.id.tag_container_layout)
        tagContainerLayout.tagTypeface = ResourcesCompat.getFont(this, R.font.proximanova_regular)
        tagContainerLayout.setOnTagClickListener(this)


        titleTextView = findViewById(R.id.text_view_title)
        messageTextView = findViewById(R.id.text_view_description)
        dateTextView = findViewById(R.id.text_view_date)
        countViewTextView = findViewById(R.id.text_view_count_view)


        imageLinearLayout = findViewById(R.id.linear_layout_image)
        imageView = findViewById(R.id.image_view)
        imageCaptionTextView = findViewById(R.id.text_view_image_caption)


        val width = resources.displayMetrics.widthPixels - 32.dpToPx()
        imageView.layoutParams.height = width * 180 / 320


        authorLinearLayout.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view) {
            toolBarFavouriteImageView -> {
                if (news.isFavourite) {
                    toolBarFavouriteImageView?.setImageResource(R.drawable.icon_favourite_add)
                    news.removeFromFavourite()
                } else {
                    toolBarFavouriteImageView?.setImageResource(R.drawable.icon_favourite)
                    news.addToFavourite()
                }
            }
            toolBarShareImageView -> {
                val shareBody = presenter.news.shareLink
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Xabardor.uz"))
            }
            authorLinearLayout -> {
                presenter.author?.let {
                    openAuthorActivity(author = it)
                }
            }
        }
    }

    override fun onItemClick(recyclerView: RecyclerView, item: News, position: Int) {
        openNewsActivity(item)
    }


    override fun onSelectedTagDrag(position: Int, text: String?) {

    }

    override fun onTagLongClick(position: Int, text: String?) {

    }

    override fun onTagClick(position: Int, text: String?) {
        text?.let { str ->
            news.tags?.find {
                if (languageManager.currentLanguage.id == Uzbek().id){
                    "#${it.title}" == str

                } else {
                    "#${it.title_cyrl}" == str
                }
            }?.let {
                openNewsListActivity(
                        tag = it
                )
            }
        }

    }

    override fun onTagCrossClick(position: Int) {

    }

    override fun onLoadingNewsDetail() {
        scrollView.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun onErrorNewsDetail() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun onSuccessNewsDetail() {
        loadWebView()

        if (languageManager.currentLanguage.id == Krill().id){
            titleTextView.setText(presenter.news.title_cyrl)
            messageTextView.setText(Html.fromHtml(presenter.news.content_cyrl))
        } else if (languageManager.currentLanguage.id == Uzbek().id){
            titleTextView.setText(presenter.news.title)
            messageTextView.setText(Html.fromHtml(presenter.news.content))
        }


        presenter.news.published?.let {
            dateTextView.text = formatDateStr(it)
        }
        countViewTextView.setText("${presenter.news.views}")
        news.tags?.let { tags ->
            tagContainerLayout.setTags(tags.map {
                if (languageManager.currentLanguage.id == Krill().id){
                    "#${it.title_cyrl}"
                } else {
                    "#${it.title}"
                }
            })
        }

        if (news.image.show) {
            imageLinearLayout.visibility = View.VISIBLE
            Glide.with(this)
                    .load(news.image.orginal)
                    .into(imageView)
            imageCaptionTextView.setText(news.image.caption)
        } else {
            imageLinearLayout.visibility = View.GONE
        }

        presenter.author?.let {
            if (!it.hidden) {
                Glide.with(this)
                        .load(it.avatar)
                        .into(authorImageView)


                authorNameTextView.setText(it.fullName)
                authorDescriptionTextView.setText(it.description)

                authorLinearLayout.visibility = View.VISIBLE
            }
        }

        relatedAdapter.onSuccess(presenter.relatedNewsList)






        toolBarFavouriteImageView?.visibility = View.VISIBLE
        toolBarShareImageView?.visibility = View.VISIBLE
        if (news.isFavourite) {
            toolBarFavouriteImageView?.setImageResource(R.drawable.icon_favourite)
        } else {
            toolBarFavouriteImageView?.setImageResource(R.drawable.icon_favourite_add)
        }


        scrollView.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }


    fun loadWebView() {

        val glide = Glide.with(this)
        glide.clear(imageViewAdsense)
        glide
            .load(presenter.adsenseTopList[0].photo)
            .into(imageViewAdsense)
        presenter.adsenseTopList[0].title?.let {
            textViewAdsense.text = it
        }
        adsenseLay.setOnClickListener {
            presenter.adsenseTopList[0].link?.let {
                openBrowser(it)
            }
        }
    }

    val news: News get() = intent.getSerializableExtra(NEWS) as News

    companion object {
        val NEWS = "news"
    }
}