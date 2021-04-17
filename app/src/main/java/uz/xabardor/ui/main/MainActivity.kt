package uz.xabardor.ui.main

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import moxy.presenter.InjectPresenter
import uz.xabardor.R
import uz.xabardor.database.TagDatabase
import uz.xabardor.extensions.*
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.ui.base.BaseActivity
import uz.xabardor.ui.base.recyclerview.OnBottomScrolledListener
import uz.xabardor.ui.base.recyclerview.OnItemClickListener
import uz.xabardor.ui.base.recyclerview.group.OnGroupRecyclerViewItemClickListener
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup


class MainActivity : BaseActivity(), MainView, OnItemClickListener<Tag>,
        OnGroupRecyclerViewItemClickListener<News>, OnTagClickListener, OnBottomScrolledListener,
    OnBannerOpenClickListener {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override val layoutId: Int
        get() = R.layout.activity_main

    lateinit var drawerLayout: DrawerLayout


    lateinit var drawerRecyclerView: RecyclerView
    lateinit var drawerAdapter: DrawerAdapter

    lateinit var recyclerView: RecyclerView
    lateinit var mainAdapter: MainAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var aboutTextView: TextView
    lateinit var contactTextView: TextView
    lateinit var favouritesTextView: TextView

    override fun setupToolbar() {
        toolBarBackImageView?.visibility = View.INVISIBLE
        toolBarMenuImageView?.visibility = View.VISIBLE
        toolBarLogoImageView?.visibility = View.VISIBLE
        toolBarSearchImageView?.visibility = View.VISIBLE


        toolBarSearchEditText?.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = textView.text.toString().trim()
                toolBarBackImageView?.callOnClick()
                if (searchText.isEmpty()) {

                    false
                } else {
                    performSearch(searchText)
                    true
                }
            } else
                false
        }

        toolBarMenuImageView?.setOnClickListener(this)
        toolBarBackImageView?.setOnClickListener(this)
    }

    override fun onCreatedView() {
        drawerLayout = findViewById(R.id.drawer_layout)

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout)

        drawerRecyclerView = findViewById(R.id.recyclerview_drawer)
        drawerAdapter = DrawerAdapter(drawerRecyclerView)
        drawerAdapter.onItemClickListener = this


        recyclerView = findViewById(R.id.recyclerview)
        mainAdapter = MainAdapter(recyclerView)
        mainAdapter.onGroupRecyclerViewItemClickListener = this
        mainAdapter.onTagClickListener = this
        mainAdapter.onBottomScrolledListener = this
        mainAdapter.onBannerOpenClickListener = this

        aboutTextView = findViewById(R.id.text_view_about)
        contactTextView = findViewById(R.id.text_view_contact)
        favouritesTextView = findViewById(R.id.text_view_favourites)



        aboutTextView.setOnClickListener(this)
        contactTextView.setOnClickListener(this)
        favouritesTextView.setOnClickListener(this)

        swipeRefreshLayout.setOnRefreshListener {
            mainAdapter.onSuccess(listOf())
            presenter.refresh()
        }


        val tags = TagDatabase.mainTags
        drawerAdapter.onSuccess(tags)

    }

    override fun onClick(view: View?) {
        when (view) {
            toolBarMenuImageView -> {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
            toolBarBackImageView -> {
                toolBarBackImageView?.visibility = View.INVISIBLE
                toolBarSearchEditText?.visibility = View.INVISIBLE
                toolBarLogoImageView?.visibility = View.VISIBLE
                toolBarMenuImageView?.visibility = View.VISIBLE


                toolBarSearchEditText?.setText("")
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            }
            toolBarSearchImageView -> {
                toolBarBackImageView?.visibility = View.VISIBLE
                toolBarSearchEditText?.visibility = View.VISIBLE
                toolBarLogoImageView?.visibility = View.INVISIBLE
                toolBarMenuImageView?.visibility = View.INVISIBLE

                toolBarSearchEditText?.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(toolBarSearchEditText, InputMethodManager.SHOW_IMPLICIT)
                }, 200)
            }
            aboutTextView -> {
                drawerLayout.closeDrawer(Gravity.LEFT)
                Handler(Looper.getMainLooper()).postDelayed({
                    openAboutUsActivity()
                }, 200)
            }
            contactTextView -> {
                drawerLayout.closeDrawer(Gravity.LEFT)
                Handler(Looper.getMainLooper()).postDelayed({
                    openContactUsActivity()
                }, 200)
            }
            favouritesTextView -> {
                drawerLayout.closeDrawer(Gravity.LEFT)
                Handler(Looper.getMainLooper()).postDelayed({
                    openFavouritesActivity()
                }, 200)
            }
        }
    }

    override fun onItemClick(recyclerView: RecyclerView, item: Tag, position: Int) {
        drawerLayout.closeDrawer(Gravity.LEFT)
        Handler(Looper.getMainLooper()).postDelayed({
            openNewsListActivity(
                    tag = item
            )
        }, 200)
    }

    override fun onBottomScrolled(recyclerView: RecyclerView) {
        presenter.getNewsList()
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

    override fun onTagClick(tag: Tag) {
        openNewsListActivity(tag = tag)
    }

    override fun onLoadingNewsList() {
        swipeRefreshLayout.isRefreshing = presenter.latestNewsList.isEmpty()
    }

    override fun onErrorNewsList(throwable: Throwable) {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onSuccessNewsList() {
        var groups = listOf<RecyclerViewGroup<News>>()
        groups = groups.plus(
                RecyclerViewGroup(
                        items = presenter.mainNewsList.filterIndexed { index, news -> index == 0 }
                )
        )

        groups = groups.plus(
                RecyclerViewGroup(
                        items = listOf(News.topBanner)
                )
        )

        groups = groups.plus(
                RecyclerViewGroup(
                        items = presenter.mainNewsList.filterIndexed { index, news -> index > 0 }
                )
        )

        groups = groups.plus(
                RecyclerViewGroup(
                        items = listOf(News.bottomBanner)
                )
        )

        groups = groups.plus(
                RecyclerViewGroup(
                        items = presenter.actualNewsList
                )
        )

        groups = groups.plus(
                RecyclerViewGroup(
                        items = presenter.latestNewsList
                )
        )

        mainAdapter.onSuccess(groups)

        swipeRefreshLayout.isRefreshing = false
    }

    fun performSearch(searchText: String) {
        openNewsListActivity(searchText = searchText)
    }

}
