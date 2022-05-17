package uz.xabardor.ui.news.list

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import uz.xabardor.R
import uz.xabardor.extensions.language.Uzbek
import uz.xabardor.extensions.openBrowser
import uz.xabardor.extensions.openNewsActivity
import uz.xabardor.extensions.openNewsListActivity
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.ui.base.BaseActivity
import uz.xabardor.ui.base.recyclerview.OnBottomScrolledListener
import uz.xabardor.ui.base.recyclerview.group.OnGroupRecyclerViewItemClickListener
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup
import uz.xabardor.ui.main.OnBannerOpenClickListener
import uz.xabardor.ui.main.OnTagClickListener

class NewsListActivity : BaseActivity(), NewsListView, OnTagClickListener,
    OnBottomScrolledListener, OnGroupRecyclerViewItemClickListener<News>, OnBannerOpenClickListener {

    @InjectPresenter
    lateinit var presenter: NewsListPresenter
    var next:Long? = null
    var total:Long? = null

    @ProvidePresenter
    fun providerPresenter() = NewsListPresenter().apply {
        tag = this@NewsListActivity.tag
        searchText = this@NewsListActivity.searchText
    }

    override val layoutId: Int
        get() = R.layout.activity_news_list


    lateinit var recyclerView: RecyclerView
    lateinit var newsListAdapter: NewsListAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var emptyTextView: TextView

    override fun setupToolbar() {
        tag?.let {
            if (languageManager.currentLanguage.id == Uzbek().id){
                toolBarTitleTextView?.setText(it.title)
            } else {
                toolBarTitleTextView?.setText(it.title_cyrl)
            }
        }

        searchText?.let {
            toolBarTitleTextView?.setText(it)
        }
    }

    override fun onCreatedView() {
        emptyTextView = findViewById(R.id.text_view_empty)
        recyclerView = findViewById(R.id.recyclerview)




        newsListAdapter = NewsListAdapter(recyclerView)
        newsListAdapter.language = languageManager.currentLanguage
        newsListAdapter.onGroupRecyclerViewItemClickListener = this
        newsListAdapter.onTagClickListener = this
        newsListAdapter.onBannerOpenClickListener = this
        newsListAdapter.onBottomScrolledListener = this

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout)
        swipeRefreshLayout.setOnRefreshListener {
            presenter.refresh()
        }

    }

    override fun onClick(p0: View?) {

    }

    override fun onItemClick(
        recyclerView: RecyclerView,
        elem: News,
        group: RecyclerViewGroup<News>,
        groupPosition: Int,
        position: Int
    ) {
        openNewsActivity(elem)
    }

    override fun onOpenBanner(url: String) {
        openBrowser(url)
    }

    override fun onBottomScrolled(recyclerView: RecyclerView) {
        if (next != null && total != null){
            if (total!! >= next!!){
                presenter.getNewsList()
            }
        }
    }


    override fun onTagClick(tag: Tag) {
        openNewsListActivity(tag = tag)
    }

    override fun onLoadingNewsList() {
        swipeRefreshLayout.isRefreshing = presenter.newsList.isEmpty()
    }

    override fun onErrorNewsList(throwable: Throwable) {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onSuccessNewsList(next: Long?, totalPages: Long?) {

        total = totalPages

        this.next = next

        swipeRefreshLayout.isRefreshing = false
//        newsListAdapter.onSuccess(presenter.newsList)

        var groups = listOf<RecyclerViewGroup<News>>()
        groups = groups.plus(
            RecyclerViewGroup(
                items = presenter.newsList.filterIndexed { index, news -> index == 0 }
            )
        )

        groups = groups.plus(
            RecyclerViewGroup(
                items = listOf(News.topBanner)
            )
        )

        groups = groups.plus(
            RecyclerViewGroup(
                items = presenter.newsList.filterIndexed { index, news -> 1 <= index && index <= 4 }
            )
        )

        groups = groups.plus(
            RecyclerViewGroup(
                items = listOf(News.bottomBanner)
            )
        )

        groups = groups.plus(
            RecyclerViewGroup(
                items = presenter.newsList.filterIndexed { index, news -> 4 < index }
            )
        )
        newsListAdapter.adsenseTop = presenter.adsenseTopList
        newsListAdapter.adsenseCenter = presenter.adsenseCenterList
        newsListAdapter.onSuccess(groups)


        if (presenter.newsList.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.INVISIBLE
        }
    }

    val tag: Tag? get() = intent.getSerializableExtra(BUNDLE_TAG) as? Tag
    val searchText: String? get() = intent.getStringExtra(BUNDLE_SEARCH_TEXT)

    companion object {
        val BUNDLE_TAG = "tag"
        val BUNDLE_SEARCH_TEXT = "search_text"
    }
}