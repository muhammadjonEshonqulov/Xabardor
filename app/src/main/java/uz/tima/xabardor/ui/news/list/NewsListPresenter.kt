package uz.tima.xabardor.ui.news.list

import moxy.InjectViewState
import uz.tima.xabardor.rest.callbacks.BaseCallback
import uz.tima.xabardor.rest.models.Adsense
import uz.tima.xabardor.rest.models.Tag
import uz.tima.xabardor.rest.models.news.News
import uz.tima.xabardor.rest.services.NewsService
import uz.tima.xabardor.ui.base.BasePresenter

@InjectViewState
class NewsListPresenter : BasePresenter<NewsListView>() {

    var tag: Tag? = null
    var searchText: String? = null

    var newsList = listOf<News>()
    var next: Long? = null
    var adsenseTopList = ArrayList<Adsense>()
    var adsenseCenterList = ArrayList<Adsense>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getAdTop()
    }


    fun refresh() {
        next = null
        newsList = listOf()
        getAdTop()
    }

    fun getNewsList() {
        tag?.let {
            getNewsListByTag()
        }

        searchText?.let {
            getNewsListBySearch()
        }
    }

    private fun getNewsListByTag() {
        NewsService.getTagNewsList(
            tag = tag!!.slug,
            next = next,
            callback = object : BaseCallback<News.ListResponse> {
                override fun onLoading() {
                    viewState.onLoadingNewsList()
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: News.ListResponse) {
                    elem.data?.newsList?.let {
                        newsList = newsList.plus(it)
                    }
                    elem.pageInfo?.nextPage?.let {
                        next = it
                    }


                    viewState.onSuccessNewsList(elem.pageInfo?.nextPage, elem.total_pages)
                }
            }
        )
    }

    private fun getNewsListBySearch() {
        NewsService.getNewsListBySearch(
            searchText = searchText!!,
            next = next,
            callback = object : BaseCallback<News.ListResponse> {
                override fun onLoading() {
                    viewState.onLoadingNewsList()
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: News.ListResponse) {
                    elem.data?.newsList?.let {
                        newsList = newsList.plus(it)
                    }
                    elem.pageInfo?.nextPage?.let {
                        next = it
                    }

                    viewState.onSuccessNewsList(next, elem.total_pages)
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
                    getNewsList()
                }

                override fun onSuccess(elem: List<Adsense>) {
                    elem.let {
                        adsenseCenterList.addAll(it)
                    }
                    getNewsList()
                }
            }
        )
    }
}