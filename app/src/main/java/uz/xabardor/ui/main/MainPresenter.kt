package uz.xabardor.ui.main

import moxy.InjectViewState
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    var latestNewsList = listOf<News>()
    var actualNewsList = listOf<News>()
    var mainNewsList = listOf<News>()

    var next: Long? = null

    override fun onFirstViewAttach() {
        getNewsList()
    }

    fun refresh() {
        latestNewsList = listOf()
        actualNewsList = listOf()
        mainNewsList = listOf()
        next = null

        getNewsList()
    }

    fun getNewsList() {
        NewsService.getLastNewsList(
                next = next,
                callback = object : BaseCallback<News.ListResponse> {
                    override fun onLoading() {
                        viewState.onLoadingNewsList()
                    }

                    override fun onError(throwable: Throwable) {
                        viewState.onErrorNewsList(throwable)
                    }

                    override fun onSuccess(elem: News.ListResponse) {
                        latestNewsList = latestNewsList.plus(elem.data.newsList)
                        next = elem.data.pageInfo.nextPage


                        if (mainNewsList.isEmpty())
                            getMainNewsList()
                        else
                            viewState.onSuccessNewsList()
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
                        mainNewsList = mainNewsList.plus(elem.data.newsList)

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
                        actualNewsList = actualNewsList.plus(elem.data.newsList)

                        viewState.onSuccessNewsList()
                    }
                }
        )
    }

}