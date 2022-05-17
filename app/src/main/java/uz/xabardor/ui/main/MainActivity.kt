package uz.xabardor.ui.main

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import moxy.presenter.InjectPresenter
import uz.xabardor.R
import uz.xabardor.database.TagDatabase
import uz.xabardor.extensions.*
import uz.xabardor.extensions.language.Krill
import uz.xabardor.extensions.language.Language
import uz.xabardor.extensions.language.Uzbek
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.models.rubric.RubricsData
import uz.xabardor.ui.base.BaseActivity
import uz.xabardor.ui.base.recyclerview.OnBottomScrolledListener
import uz.xabardor.ui.base.recyclerview.OnItemClickListener
import uz.xabardor.ui.base.recyclerview.group.OnGroupRecyclerViewItemClickListener
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup
import uz.xabardor.extensions.openNewsListActivity as openNewsListActivity1


class MainActivity : BaseActivity(), MainView, OnItemClickListener<RubricsData>,
        OnGroupRecyclerViewItemClickListener<News>, OnTagClickListener, OnBottomScrolledListener,
    OnBannerOpenClickListener {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override val layoutId: Int
        get() = R.layout.activity_main
    lateinit var prefss:Prefss
    lateinit var language: Language

    lateinit var drawerLayout: DrawerLayout
    var groups = ArrayList<RecyclerViewGroup<News>>()

    lateinit var drawerRecyclerView: RecyclerView
    lateinit var drawerAdapter: DrawerAdapter
//    lateinit var rubricsAdapter : RubricsAdapter
    lateinit var languageUzbek: LinearLayout
    lateinit var languageKrill: LinearLayout

    lateinit var recyclerView: RecyclerView
//    lateinit var recyclerViewRubrics: RecyclerView
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreatedView() {
        prefss = Prefss(this)
        drawerLayout = findViewById(R.id.drawer_layout)

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout)

        drawerRecyclerView = findViewById(R.id.recyclerview_drawer)
        languageUzbek = findViewById(R.id.language_uzbek)
        languageKrill = findViewById(R.id.language_krill)

        if (languageManager.currentLanguage.id == Uzbek().id){
            languageUzbek.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
            languageKrill.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
        } else if (languageManager.currentLanguage.id == Krill().id){
            languageKrill.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
            languageUzbek.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
            }

        drawerAdapter = DrawerAdapter(drawerRecyclerView)
        drawerAdapter.language = languageManager.currentLanguage
        drawerAdapter.onItemClickListener = this


        recyclerView = findViewById(R.id.recyclerview)
//        recyclerViewRubrics = findViewById(R.id.list_rubrics)
        mainAdapter = MainAdapter(recyclerView)
        mainAdapter.language = languageManager.currentLanguage
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
        languageUzbek.setOnClickListener(this)
        languageKrill.setOnClickListener(this)

        swipeRefreshLayout.setOnRefreshListener {
            mainAdapter.onSuccess(listOf())
            groups.clear()
            presenter.refresh()
        }


//        val tags = TagDatabase.mainTags
        val rubrics = TagDatabase.mainRubrics
        drawerAdapter.onSuccess(rubrics)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
            languageKrill -> {
                language = Krill()
                prefss.save(prefss.language, Language.UZ)
                languageManager.currentLanguage = language
                languageKrill.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                languageUzbek.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
                contactTextView.text = "Биз билан алоқа"
                aboutTextView.text = "Биз ҳақимизда"
                favouritesTextView.text = "Танланган мақолалар"
                mainAdapter.language = language
                mainAdapter.notifyDataSetChanged()
                drawerAdapter.language = language
                drawerAdapter.notifyDataSetChanged()
            }
            languageUzbek -> {
                language = Uzbek()
                prefss.save(prefss.language, Language.KR)
                languageManager.currentLanguage = language
                languageUzbek.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                languageKrill.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
                mainAdapter.language = language
                mainAdapter.notifyDataSetChanged()
                contactTextView.text = "Biz bilan aloqa"
                aboutTextView.text = "Biz haqimizda"
                favouritesTextView.text = "Tanlangan maqolalar"
                drawerAdapter.language = language
                drawerAdapter.notifyDataSetChanged()
                toolBarSearchImageView
//                rubricsAdapter.language = language
//                rubricsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(recyclerView: RecyclerView, item: RubricsData, position: Int) {
        drawerLayout.closeDrawer(Gravity.LEFT)
        Handler(Looper.getMainLooper()).postDelayed({
//            openNewsListActivity1(
//                rubricsData = item
//            )
            if (!swipeRefreshLayout.isRefreshing) {
                presenter.tag = "" + item.slug
                presenter.type = "rubric"
                groups.clear()
                mainAdapter.onSuccess(listOf())
                presenter.refresh()
                swipeRefreshLayout.isRefreshing = true
            }
        }, 200)
    }

    override fun onBottomScrolled(recyclerView: RecyclerView) {
        presenter.getNewsList(presenter.type, presenter.tag)
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
        openNewsListActivity1(tag = tag)
    }

    override fun onLoadingNewsList() {
        swipeRefreshLayout.isRefreshing = presenter.latestNewsList.isEmpty()
    }

    override fun onErrorNewsList(throwable: Throwable) {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onSuccessNewsList() {
//        var groups = listOf<RecyclerViewGroup<News>>()


        groups.add(
            RecyclerViewGroup(
                items = presenter.mainNewsList.filterIndexed { index, news -> index == 0 }
            )
        )

        groups.add(
            RecyclerViewGroup(
                items = listOf(News.topBanner)
            )
        )

        groups.add(
            RecyclerViewGroup(
                items = presenter.mainNewsList.filterIndexed { index, news -> index > 0 }
            )
        )

        groups.add(
            RecyclerViewGroup(
                items = listOf(News.bottomBanner)
            )
        )

        groups.add(
            RecyclerViewGroup(
                items = presenter.actualNewsList.toList()
            )
        )
        groups.add(
            RecyclerViewGroup(
                items = presenter.latestNewsList.toList()
            )
        )
        mainAdapter.adsenseTop = presenter.adsenseTopList
        mainAdapter.adsenseCenter = presenter.adsenseCenterList
        mainAdapter.onSuccess(groups.toList())

        swipeRefreshLayout.isRefreshing = false
    }

    override fun onSuccessRubrics(rubrics: List<RubricsData>) {
        if (rubrics.isNotEmpty()) {
            var arrayRubrics = ArrayList<RubricsData>()
            arrayRubrics.addAll(rubrics)
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//            rubricsAdapter.setOnClickListener {
//                if (!swipeRefreshLayout.isRefreshing) {
//                    presenter.tag = "" + it.slug
//                    presenter.type = "rubric"
//                    groups.clear()
//                    mainAdapter.onSuccess(listOf())
//                    presenter.refresh()
//                    swipeRefreshLayout.isRefreshing = true
//                }
//            }
        }
    }

    fun performSearch(searchText: String) {
        openNewsListActivity1(rubricsData = null, searchText = searchText)
    }

}
