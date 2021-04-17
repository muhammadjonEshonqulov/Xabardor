package uz.xabardor.ui.news.author

import moxy.InjectViewState
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.AuthorService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class AuthorPresenter : BasePresenter<AuthorView>() {

    lateinit var author: Author
    var tags = listOf<Tag>()
    var selectedTag: Tag? = null

    var newsList = listOf<News>()
    var next: Long? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()


        getAuthorInfo()
    }

    fun refresh() {
        newsList = listOf()
        next = null

        getAuthorNewsList()
    }

    fun getAuthorInfo() {
        AuthorService.getAuthorInfo(
                nickName = author.nickName,
                callback = object : BaseCallback<Author.DetailResponse> {
                    override fun onLoading() {
                        viewState.onLoadingAuthorInfo()
                    }

                    override fun onError(throwable: Throwable) {
                        viewState.onErrorAuthorInfo(throwable)
                    }

                    override fun onSuccess(elem: Author.DetailResponse) {
                        tags = tags.plus(Tag(
                                title = "",
                                slug = "",
                                id = -1
                        ))
                        tags = tags.plus(elem.data.tags)
                        viewState.onSuccessAuthorInfo()
                        getAuthorNewsList()
                    }
                }
        )
    }

    fun getAuthorNewsList() {
        AuthorService.getAuthorNewsList(
                next = next,
                nickName = author.nickName,
                tag = selectedTag?.slug,
                callback = object : BaseCallback<News.ListResponse> {
                    override fun onLoading() {
                        viewState.onLoadingAuthorNewsList()
                    }

                    override fun onError(throwable: Throwable) {
                        viewState.onErrorAuthorNewsList(throwable)
                    }

                    override fun onSuccess(elem: News.ListResponse) {
                        newsList = newsList.plus(elem.data.newsList)
                        next = elem.data.pageInfo.nextPage
                        viewState.onSuccessAuthorNewsList()
                    }
                }
        )
    }

}