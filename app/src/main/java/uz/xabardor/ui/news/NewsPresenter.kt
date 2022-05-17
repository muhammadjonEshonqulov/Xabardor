package uz.xabardor.ui.news

import android.util.Log
import moxy.InjectViewState
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.Adsense
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class NewsPresenter : BasePresenter<NewsView>() {

    lateinit var news: News
    var author: Author? = null
    var relatedNewsList = listOf<News>()
    var adsenseTopList = ArrayList<Adsense>()

    override fun onFirstViewAttach() {
        getAdTop()
    }

    fun getAdTop() {
        NewsService.getAdTop(
            callback = object : BaseCallback<List<Adsense>> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsDetail()
                    getNewsDetail()
                }

                override fun onSuccess(elem: List<Adsense>) {
                    elem.let {
                        adsenseTopList.addAll(it)
                    }

                    getNewsDetail()
//                    viewState.onSuccessNewsList()
                }
            }
        )
    }


    fun getNewsDetail() {
        NewsService.getNewsDetail(
            newsShortCode = news.shortCode,
            callback = object : BaseCallback<News.DetailResponse> {
                override fun onLoading() {
                    viewState.onLoadingNewsDetail()
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsDetail()
                }

                override fun onSuccess(elem: News.DetailResponse) {
                    elem.related?.let {
                        relatedNewsList = it
                    }
                    elem.news?.apply {
                        this@NewsPresenter.news = this
                    }
//                        author = elem.data.author
                    viewState.onSuccessNewsDetail()
                }

            }
        )
    }

}