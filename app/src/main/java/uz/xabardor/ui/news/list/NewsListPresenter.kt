package uz.xabardor.ui.news.list

import moxy.InjectViewState
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class NewsListPresenter : BasePresenter<NewsListView>() {

    var tag: Tag? = null
    var searchText: String? = null

    var newsList = listOf<News>()
    var next: Long? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getNewsList()
    }


    fun refresh() {
        next = null
        newsList = listOf()
        getNewsList()
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
                        newsList = newsList.plus(elem.data.newsList)
                        next = elem.data.pageInfo.nextPage

                        viewState.onSuccessNewsList()
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
                        newsList = newsList.plus(elem.data.newsList)
                        next = elem.data.pageInfo.nextPage

                        viewState.onSuccessNewsList()
                    }
                }
        )
    }

}