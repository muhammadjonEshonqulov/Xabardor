package uz.xabardor.ui.news

import moxy.InjectViewState
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class NewsPresenter : BasePresenter<NewsView>() {

    lateinit var news: News
    var author: Author? = null
    var relatedNewsList = listOf<News>()

    override fun onFirstViewAttach() {
        getNewsDetail()
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
                        relatedNewsList = elem.data.related
                        elem.data.info.apply{
                            this@NewsPresenter.news = this
                        }
                        author = elem.data.author
                        viewState.onSuccessNewsDetail()
                    }

                }
        )
    }

}