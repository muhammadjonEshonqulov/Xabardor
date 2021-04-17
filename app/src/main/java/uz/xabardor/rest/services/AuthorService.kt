package uz.xabardor.rest.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News

object AuthorService : BaseService() {

    fun getAuthorInfo(
            nickName: String,
            callback: BaseCallback<Author.DetailResponse>
    ) {

        callback.onLoading()
        NewsService.api.getAuthorInfo(
                nickName = nickName
        ).enqueue(object : Callback<Author.DetailResponse> {
            override fun onFailure(call: Call<Author.DetailResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<Author.DetailResponse>, response: Response<Author.DetailResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getAuthorNewsList(
            nickName: String,
            tag: String? = null,
            next: Long? = null,
            callback: BaseCallback<News.ListResponse>
    ) {

        callback.onLoading()
        NewsService.api.getAuthorNewsList(
                nickName = nickName,
                next = next,
                type = if (tag == null) null else "tag",
                tag = tag
        ).enqueue(object : Callback<News.ListResponse> {
            override fun onFailure(call: Call<News.ListResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<News.ListResponse>, response: Response<News.ListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }
}