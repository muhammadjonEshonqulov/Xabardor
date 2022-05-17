package uz.xabardor.ui.main

import moxy.InjectViewState
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.Adsense
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.models.rubric.RubricsResponse
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    var latestNewsList = ArrayList<News>()
    var actualNewsList = ArrayList<News>()
    var adsenseTopList = ArrayList<Adsense>()
    var adsenseCenterList = ArrayList<Adsense>()
    var mainNewsList = ArrayList<News>()

    var next: Long? = null
    var tag = "last"
    var type = ""

    override fun onFirstViewAttach() {
        getAdTop()
        getRubrics()
      //  getNewsList("", tag)
    }

    fun refresh() {
        latestNewsList.clear()
        actualNewsList.clear()
        mainNewsList.clear()
        adsenseCenterList.clear()
        adsenseTopList.clear()
        next = null
        getAdTop()
    }

    fun getRubrics() {
        NewsService.getRubrics(
            callback = object : BaseCallback<RubricsResponse> {
                override fun onLoading() {
                    viewState.onLoadingNewsList()
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: RubricsResponse) {
                    elem.data?.rubrics?.let {
                        viewState.onSuccessRubrics(it)
                    }
                }
            }
        )
    }

    fun getNewsList(
        type: String,
        tag: String
    ) {
        NewsService.getLastNewsList(
            next = next,
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
                    elem.data?.newsList?.let {
                        latestNewsList.addAll(it)
                    }
                    next = elem.pageInfo?.nextPage


                    if (tag == "last") {
                        elem.data?.newsList?.let {
                            latestNewsList.addAll(it)
                        }
                        if (mainNewsList.isEmpty())
                            getMainNewsList()
                    } else {
                        elem.data?.newsList?.let {
                            mainNewsList.addAll(it)
                        }
                        viewState.onSuccessNewsList()
                    }

                }
            }
        )
    }

    fun getMainNewsList() {
        NewsService.getMainNewsList(
            callback = object : BaseCallback<News.ListResponse> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: News.ListResponse) {
                    elem.data?.newsList?.let {
                        mainNewsList.addAll(it)
                    }

//                    viewState.onSuccessNewsList()
                    getActualNewsList()
                }
            }
        )
    }

    fun getActualNewsList() {
        NewsService.getActualNewsList(
            callback = object : BaseCallback<News.ListResponse> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: News.ListResponse) {
                    elem.data?.newsList?.let {
                        actualNewsList.addAll(it)
                    }

                    viewState.onSuccessNewsList()
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
//                    viewState.onSuccessNewsList()
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
                    getNewsList(type, tag)
                }

                override fun onSuccess(elem: List<Adsense>) {
                    elem.let {
                        adsenseCenterList.addAll(it)
                    }
                    getNewsList(type, tag)
                }
            }
        )
    }
}