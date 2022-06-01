package uz.xabardor.ui.main

import uz.xabardor.rest.models.WeathersAppResponse
import moxy.InjectViewState
import uz.xabardor.extensions.lg
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.Adsense
import uz.xabardor.rest.models.ExchangeRatesData
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    var mainNewsList = ArrayList<News>()
    var latestNewsList = ArrayList<News>()
    var actualNewsList = ArrayList<News>()
    var adsenseTopList = ArrayList<Adsense>()
    var adsenseCenterList = ArrayList<Adsense>()
    var adsenseAfterTrendList = ArrayList<Adsense>()

    var nextNews: Long? = null
    var nextActual: Long? = null
    var nextMain: Long? = null
    var tag = "last"
    var type = ""

    override fun onFirstViewAttach() {
        getExchangeRates()
        getAdTop()
//        getRubrics()
      //  getNewsList("", tag)
    }

    fun refresh() {
        latestNewsList.clear()
        actualNewsList.clear()
        mainNewsList.clear()
        adsenseCenterList.clear()
        adsenseTopList.clear()
        adsenseAfterTrendList.clear()
        nextMain = null
        nextActual = null
        nextNews = null
        getAdTop()
    }

//    fun getRubrics() {
//        NewsService.getRubrics(
//            callback = object : BaseCallback<RubricsResponse> {
//                override fun onLoading() {
//                    viewState.onLoadingNewsList()
//                }
//
//                override fun onError(throwable: Throwable) {
//                    viewState.onErrorNewsList(throwable)
//                }
//
//                override fun onSuccess(elem: RubricsResponse) {
//                    elem.data?.rubrics?.let {
//                        viewState.onSuccessRubrics(it)
//                    }
//                }
//            }
//        )
//    }

    fun getExchangeRates() {
        NewsService.getExchangeRates(
            callback = object : BaseCallback<List<ExchangeRatesData>> {
                override fun onLoading() {
                    viewState.onLoadingNewsList()
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: List<ExchangeRatesData>) {
                    elem.let {
                        getWeather(it)
                    }
                }
            }
        )
    }

    fun getWeather(list: List<ExchangeRatesData>) {
        NewsService.getWeather(
            callback = object : BaseCallback<WeathersAppResponse> {
                override fun onLoading() {
                    viewState.onLoadingNewsList()
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: WeathersAppResponse) {

                    elem.let {
                        viewState.onSuccessTopWeatherAndExchanges(list, it)
                    }
                }
            }
        )
    }

    fun getNewsList(
        type: String,
        tag: String,
        groupPosition:Int? = null
    ) {
        NewsService.getLastNewsList(
            next = nextNews,
            type = type,
            tag = tag,
            callback = object : BaseCallback<News.ListResponse> {
                override fun onLoading() {
                    viewState.onLoadingNewsList()
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: News.ListResponse) {

                    if (groupPosition == null){
                        if (tag == "last") {
                            nextNews = elem.pageInfo?.nextPage

                            elem.data?.newsList?.let {
                                latestNewsList.addAll(it)
                            }
                            if (mainNewsList.isEmpty()){
                                getMainNewsList()
                            } else {
                                viewState.onSuccessNewsList(tag = tag)
                            }
                        } else {
                            nextNews = elem.pageInfo?.nextPage
                            elem.data?.newsList?.let {
                                mainNewsList.clear()
                                mainNewsList.addAll(it)
                            }
                            viewState.onSuccessNewsList(tag = tag)
                        }
                    } else {
                        elem.data?.newsList?.let {
                            if (groupPosition == 2)
                            latestNewsList.clear()
                            latestNewsList.addAll(it) }
                        viewState.onSuccessMore(latestNewsList, groupPosition)
                    }

                }
            }
        )
    }

    fun getMainNewsList(groupPosition:Int? = null) {
        NewsService.getMainNewsList(
            next = nextMain,
            callback = object : BaseCallback<News.ListResponse> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: News.ListResponse) {
                    nextMain = elem.pageInfo?.nextPage
                    elem.data?.newsList?.let {
                        mainNewsList.addAll(it)
                    }
                    if (groupPosition == null){
                        viewState.onSuccessNewsList(tag = tag)
                    } else {
                     viewState.onSuccessMore(mainNewsList, groupPosition)
                    }

                }
            }
        )
    }

    fun getActualNewsList(tag: String? = null, groupPosition:Int? = null) {
        NewsService.getActualNewsList(
            next = nextActual,
            callback = object : BaseCallback<News.ListResponse> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: News.ListResponse) {
                    nextActual = elem.pageInfo?.nextPage

                    elem.data?.newsList?.let {
                        actualNewsList.addAll(it)
                    }

//                    if (groupPosition == null){
                        viewState.onSuccessNewsList(tag = tag)
//                    } else {
//                        viewState.onSuccessMore(actualNewsList, groupPosition)
//                    }
                }
            }
        )
    }

    fun getAdTop() {
        NewsService.getAdTop(
            callback = object : BaseCallback<List<Adsense>> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                    getAdCenter()
                }

                override fun onSuccess(elem: List<Adsense>) {
                    elem.let {
                        adsenseTopList.addAll(it)
                    }

                    getAdCenter()
                }
            }
        )
    }

    fun getAdCenter() {
        NewsService.getAdCenter(
            callback = object : BaseCallback<List<Adsense>> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                    getAfterTrend()
                }

                override fun onSuccess(elem: List<Adsense>) {
                    elem.let {
                        adsenseCenterList.addAll(it)
                    }
                    getAfterTrend()
                }
            }
        )
    }
    fun getAfterTrend() {
        NewsService.getAfterTrend(
            callback = object : BaseCallback<List<Adsense>> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                    getNewsList(type, tag)
                }

                override fun onSuccess(elem: List<Adsense>) {
                    elem.let {
                        adsenseAfterTrendList.addAll(it)
                    }
                    getNewsList(type, tag)
                }
            }
        )
    }
}