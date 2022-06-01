package uz.xabardor.ui.main

import uz.xabardor.rest.models.WeathersAppResponse
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import moxy.presenter.InjectPresenter
import uz.xabardor.R
import uz.xabardor.database.TagDatabase
import uz.xabardor.extensions.*
import uz.xabardor.extensions.language.Krill
import uz.xabardor.extensions.language.Language
import uz.xabardor.extensions.language.Uzbek
import uz.xabardor.rest.models.ExchangeRatesData
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.models.rubric.RubricsData
import uz.xabardor.rest.models.weather.Viloyat
import uz.xabardor.ui.base.BaseActivity
import uz.xabardor.ui.base.recyclerview.OnBottomScrolledListener
import uz.xabardor.ui.base.recyclerview.OnItemClickListener
import uz.xabardor.ui.base.recyclerview.group.OnGroupRecyclerViewItemClickListener
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup
import uz.xabardor.extensions.openNewsListActivity as openNewsListActivity1
class MainActivity : BaseActivity(), MainView, OnItemClickListener<RubricsData>,OnMoreClickListener,
        OnGroupRecyclerViewItemClickListener<News>,DrawerAdapter.OnItemClickListener, OnTagClickListener, OnBottomScrolledListener,
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
    var weatherAdapter : WeatherAdapter? = null
    lateinit var logo_main: ImageView
//    lateinit var rubricsAdapter : RubricsAdapter
    lateinit var languageUzbek: LinearLayout
    lateinit var languageKrill: LinearLayout

    lateinit var recyclerView: RecyclerView
//    lateinit var recyclerViewRubrics: RecyclerView
    lateinit var mainAdapter: MainAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var aboutTextView: TextView
    lateinit var nightText: TextView
    lateinit var dayText: TextView
    lateinit var contactTextView: TextView
    lateinit var exchange: TextView
    lateinit var rubric_name: TextView
    lateinit var favouritesTextView: TextView
    lateinit var actualTheme: TextView

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
        toolBarLogoImageView?.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreatedView() {
        prefss = Prefss(this)
        drawerLayout = findViewById(R.id.drawer_layout)

        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout)

        drawerRecyclerView = findViewById(R.id.recyclerview_drawer)
        logo_main = findViewById(R.id.logo_main)
        languageUzbek = findViewById(R.id.language_uzbek)
        exchange = findViewById(R.id.exchange)
        rubric_name = findViewById(R.id.rubric_name)
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
        drawerAdapter.onItemClickListenerr = this


        recyclerView = findViewById(R.id.recyclerview)
//        recyclerViewRubrics = findViewById(R.id.list_rubrics)
        mainAdapter = MainAdapter(recyclerView)
        mainAdapter.language = languageManager.currentLanguage
        mainAdapter.onGroupRecyclerViewItemClickListener = this
        mainAdapter.onTagClickListener = this
        mainAdapter.onMoreClickListener = this
        mainAdapter.onBottomScrolledListener = this
        mainAdapter.onBannerOpenClickListener = this

        aboutTextView = findViewById(R.id.text_view_about)
        dayText = findViewById(R.id.day_text)
        nightText = findViewById(R.id.night_text)
        contactTextView = findViewById(R.id.text_view_contact)
        favouritesTextView = findViewById(R.id.text_view_favourites)
        actualTheme = findViewById(R.id.actual_theme)
        onCreateLanguage(languageManager.currentLanguage)



        aboutTextView.setOnClickListener(this)
        aboutTextView.setOnClickListener(this)
        logo_main.setOnClickListener(this)
        contactTextView.setOnClickListener(this)
        favouritesTextView.setOnClickListener(this)
        actualTheme.setOnClickListener(this)
        languageUzbek.setOnClickListener(this)
        languageKrill.setOnClickListener(this)

        swipeRefreshLayout.setOnRefreshListener {
            if (rubric_name.text == getString(R.string.actual_theme))
            rubric_name.visibility = View.GONE
            mainAdapter.onSuccess(listOf())
            groups.clear()
            presenter.refresh()
        }


//        val tags = TagDatabase.mainTags
        val rubrics = ArrayList<RubricsData>()
        TagDatabase.mainRubrics.let {
            it.forEachIndexed { index, rubricsData ->
                   if (rubricsData.parent.isNullOrEmpty()){
                       rubrics.add(rubricsData)
                   } else {
                       rubrics.forEachIndexed { index2, rubricsData2 ->
                           if (rubricsData.parent.toInt() == rubricsData2.id){
                               if(rubricsData2.rubrics == null){
                                   rubricsData2.rubrics = ArrayList()
                               }
                               rubrics.get(index2).rubrics?.add(rubricsData)
                           }
                       }
                   }
                }
            }
        drawerAdapter.onSuccess(rubrics)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(view: View?) {
        when (view) {
            toolBarMenuImageView -> {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
            toolBarLogoImageView -> {
                presenter.tag = "last"
                presenter.type = ""
                rubric_name.text = ""
                rubric_name.visibility = View.GONE
                mainAdapter.onSuccess(listOf())
                groups.clear()
                presenter.refresh()
                drawerLayout.closeDrawer(Gravity.LEFT)
            }
            toolBarBackImageView -> {
                toolBarBackImageView?.visibility = View.INVISIBLE
                toolBarSearchEditText?.visibility = View.INVISIBLE
                toolBarLogoImageView?.visibility = View.VISIBLE
                toolBarMenuImageView?.visibility = View.VISIBLE


                toolBarSearchEditText?.setText("")
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            }
            logo_main -> {
                presenter.tag = "last"
                presenter.type = ""
                rubric_name.text = ""
                rubric_name.visibility = View.GONE
                mainAdapter.onSuccess(listOf())
                groups.clear()
                presenter.refresh()
                drawerLayout.closeDrawer(Gravity.LEFT)
            }
            toolBarSearchImageView -> {
                if (toolBarSearchEditText?.isFocused == true){
                    if (toolBarSearchEditText?.text.toString().trim().isNotEmpty()){
                        performSearch(toolBarSearchEditText?.text.toString().trim())
                    }
                } else {
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
            actualTheme -> {
                drawerLayout.closeDrawer(Gravity.LEFT)
                presenter.nextActual = null
                groups.clear()
                rubric_name.visibility = View.VISIBLE
                rubric_name.text = getString(R.string.actual_theme)
                presenter.getActualNewsList("selected")
//                Handler(Looper.getMainLooper()).postDelayed({
//                    openFavouritesActivity()
//                }, 200)
            }
            languageKrill -> {
                language = Krill()
                prefss.save(prefss.language, Language.UZ)
                languageManager.currentLanguage = language
                languageKrill.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                languageUzbek.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
                notifyLanguageChanged()
//                contactTextView.text = "Биз билан алоқа"
//                aboutTextView.text = "Биз ҳақимизда"
//                favouritesTextView.text = "Танланган мақолалар"
//                actualTheme.text = "Энг кўп ўқилганлар"
//                toolBarSearchEditText?.hint = "Қидириш"
                mainAdapter.language = language
                mainAdapter.notifyDataSetChanged()
                drawerAdapter.language = language
                drawerAdapter.notifyDataSetChanged()
                weatherAdapter?.notifyDataSetChanged()

            }
            languageUzbek -> {
                language = Uzbek()
                prefss.save(prefss.language, Language.KR)
                languageManager.currentLanguage = language
                notifyLanguageChanged()
                languageUzbek.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
                languageKrill.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite))
                notifyLanguageChanged()
                mainAdapter.language = language
                mainAdapter.notifyDataSetChanged()
                drawerAdapter.language = language
                drawerAdapter.notifyDataSetChanged()
                weatherAdapter?.notifyDataSetChanged()

            }
        }
    }

    override fun onCreateLanguage(language: Language) {
        super.onCreateLanguage(language)
        contactTextView.text = getString(R.string.contact_us)
        aboutTextView.text = getString(R.string.about_us)
        favouritesTextView.text = getString(R.string.favourites)
        actualTheme.text = getString(R.string.actual_theme)
        toolBarSearchEditText?.hint = getString(R.string.search)
        dayText.text = getString(R.string.day)
        nightText.text = getString(R.string.night)
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
                presenter.nextNews = null
                groups.clear()
                mainAdapter.onSuccess(listOf())
                presenter.refresh()
                swipeRefreshLayout.isRefreshing = true
            }
        }, 200)
    }

    override fun onBottomScrolled(recyclerView: RecyclerView) {
//        presenter.getNewsList(presenter.type, presenter.tag)
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

    override fun onSuccessNewsList(tag:String?) {
//        var groups = listOf<RecyclerViewGroup<News>>()
        if (tag == "last"){
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
                    items = presenter.latestNewsList.toList().sortedBy { it.published_at }, next = presenter.nextNews
                )
            )
            groups.add(
                RecyclerViewGroup(
                    items = listOf(News.bottomBanner)
                )
            )
            mainAdapter.selected = ""
        } else if (tag == "selected" ){
            groups.add(
                RecyclerViewGroup(
                    items = presenter.actualNewsList.filterIndexed { index, news -> index == 0 }
                )
            )

            groups.add(
                RecyclerViewGroup(
                    items = listOf(News.topBanner)
                )
            )
            groups.add(
                RecyclerViewGroup(
                    items = presenter.actualNewsList.filterIndexed { index, news -> index > 0 }.sortedBy { it.published_at }, next = presenter.nextNews
                )
            )
            groups.add(
                RecyclerViewGroup(
                    items = listOf(News.bottomBanner)
                )
            )
            mainAdapter.selected = "selected"
        } else {
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
                    items = presenter.mainNewsList.filterIndexed { index, news -> index > 0 }.sortedBy { it.published_at }, next = presenter.nextNews
                )
            )
            groups.add(
                RecyclerViewGroup(
                    items = listOf(News.bottomBanner)
                )
            )
            mainAdapter.selected = ""
        }

        mainAdapter.adsenseTop = presenter.adsenseTopList
        mainAdapter.adsenseCenter = presenter.adsenseCenterList
        mainAdapter.adsenseAfterTrendList = presenter.adsenseAfterTrendList
        mainAdapter.onSuccess(groups.toList())

        swipeRefreshLayout.isRefreshing = false
    }

    override fun onSuccessTopWeatherAndExchanges(
        list: List<ExchangeRatesData>,
        list1: WeathersAppResponse
    ) {
        val regions = ArrayList<Viloyat>()
        regions.add(Viloyat(list1.andijan.img, list1.andijan.day, list1.andijan.night, list1.andijan.uz, list1.andijan.uz_cyrl))
        regions.add(Viloyat(list1.bukhara.img, list1.bukhara.day, list1.bukhara.night, list1.bukhara.uz, list1.bukhara.uz_cyrl))
        regions.add(Viloyat(list1.fergana.img, list1.fergana.day, list1.fergana.night, list1.fergana.uz, list1.fergana.uz_cyrl))
        regions.add(Viloyat(list1.gulistan.img, list1.gulistan.day, list1.gulistan.night, list1.gulistan.uz, list1.gulistan.uz_cyrl))
        regions.add(Viloyat(list1.jizzakh.img, list1.jizzakh.day, list1.jizzakh.night, list1.jizzakh.uz, list1.jizzakh.uz_cyrl))
        regions.add(Viloyat(list1.karshi.img, list1.karshi.day, list1.karshi.night, list1.karshi.uz, list1.karshi.uz_cyrl))
        regions.add(Viloyat(list1.namangan.img, list1.namangan.day, list1.namangan.night, list1.namangan.uz, list1.namangan.uz_cyrl))
        regions.add(Viloyat(list1.navoiy.img, list1.navoiy.day, list1.navoiy.night, list1.navoiy.uz, list1.navoiy.uz_cyrl))
        regions.add(Viloyat(list1.nukus.img, list1.nukus.day, list1.nukus.night, list1.nukus.uz, list1.nukus.uz_cyrl))
        regions.add(Viloyat(list1.samarkand.img, list1.samarkand.day, list1.samarkand.night, list1.samarkand.uz, list1.samarkand.uz_cyrl))
        regions.add(Viloyat(list1.tashkent.img, list1.tashkent.day, list1.tashkent.night, list1.tashkent.uz, list1.tashkent.uz_cyrl))
        regions.add(Viloyat(list1.termez.img, list1.termez.day, list1.termez.night, list1.termez.uz, list1.termez.uz_cyrl))
        regions.add(Viloyat(list1.urgench.img, list1.urgench.day, list1.urgench.night, list1.urgench.uz, list1.urgench.uz_cyrl))

        weatherAdapter = WeatherAdapter(this, regions)
        val spinner_weather = findViewById<Spinner>(R.id.spinner_weather)
        spinner_weather.adapter = weatherAdapter
        spinner_weather.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                findViewById<TextView>(R.id.weather_night).text = ""+regions.get(position).night+"°"
                findViewById<TextView>(R.id.weather_day).text = ""+regions.get(position).day+"°"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        var text = ""
        for (i in 0..2){
            text += if(list.get(i).Ccy == "USD") " $ " else if(list.get(i).Ccy == "EUR") " € " else if(list.get(i).Ccy == "RUB") " ₽ " else "  "
            text += "  "+list.get(i).Rate + if(list.get(i).Diff?.toFloat()!! < i) "  UZS  \uD83D\uDD3B "+list.get(i).Diff +"  " else "  \uD83D\uDD3C "+list.get(i).Diff+"   "
        }
        exchange.text = text
        exchange.isSelected = true


    }

//    override fun onSuccessTopWeatherAndExchanges(list: List<ExchangeRatesData>, list1: WeatherResponse) {
//        exchange.text = "$ "+list.get(0).Rate + if(list.get(0).Diff?.toInt()!! < 0) "\uD83D\uDD3D" else "\uD83D\uDD3C "+list.get(0).Diff
//    }
    override fun onSuccessMore(data: List<News>, groupPosition: Int) {
//        groups.get(groupPosition).items = data
    if (groupPosition == 2)
         groups.add(
        RecyclerViewGroup(
            items = presenter.latestNewsList.toList().sortedBy { it.published_at }, next = presenter.nextNews
           )
         )
    else
        groups.get(groupPosition).items = data
        mainAdapter.onSuccess(groups)

    }

    fun performSearch(searchText: String) {
        openNewsListActivity1(rubricsData = null, searchText = searchText)
    }

    override fun onItemClick(position: Int, data: RubricsData) {
        if (position == -1){
            drawerLayout.closeDrawer(Gravity.LEFT)
            Handler(Looper.getMainLooper()).postDelayed({
//            openNewsListActivity1(
//                rubricsData = item
//            )
                if (!swipeRefreshLayout.isRefreshing) {
                    presenter.tag = "" + data.slug
                    presenter.type = "rubric"
                    presenter.nextNews = null
                    groups.clear()
                    mainAdapter.onSuccess(listOf())
                    presenter.getNewsList("rubric", "" + data.slug)
                    swipeRefreshLayout.isRefreshing = true
                }
            }, 200)

            rubric_name.visibility = View.VISIBLE
            if (languageManager.currentLanguage.id == Uzbek().id){
                rubric_name.text = data.title?.capitalize()
            } else if (languageManager.currentLanguage.id == Krill().id){
                rubric_name.text = data.title_cyrl?.capitalize()
            }
        } else {
            drawerAdapter.notifyItemChanged(position)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView.adapter = null
    }

    override fun onMoreClick(groupPosition: Int) {
//        when (groupPosition) {
////            2 -> {
////                presenter.getMainNewsList(groupPosition)
////            }
////            4 -> {
////                presenter.getActualNewsList(groupPosition)
////            }
//            2 -> {
                presenter.getNewsList(presenter.type, presenter.tag, groupPosition = groupPosition )
//            }
//        }
    }
}


