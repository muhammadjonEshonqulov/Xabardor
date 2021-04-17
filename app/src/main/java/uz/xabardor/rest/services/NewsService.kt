package uz.xabardor.rest.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.news.News

object NewsService : BaseService() {

    fun getNewsList(
            next: Long? = null,
            callback: BaseCallback<News.ListResponse>
    ) {
        callback.onLoading()
        api.getNewsList(
                next = next
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

    fun getLastNewsList(
            next: Long? = null,
            callback: BaseCallback<News.ListResponse>
    ) {
        callback.onLoading()
        api.getNewsList(
                type = "last",
                next = next
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

    fun getTagNewsList(
            next: Long? = null,
            tag: String,
            callback: BaseCallback<News.ListResponse>
    ) {


        callback.onLoading()
        api.getNewsList(
                next = next,
                type = "tag",
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

    fun getMainNewsList(
            next: Long? = null,
            callback: BaseCallback<News.ListResponse>
    ) {

        callback.onLoading()
        api.getNewsList(
                type = "main",
                next = next
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

    fun getActualNewsList(
            next: Long? = null,
            callback: BaseCallback<News.ListResponse>
    ) {

        callback.onLoading()
        api.getNewsList(
                type = "selected",
                next = next
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

    fun getNewsListBySearch(
            next: Long? = null,
            searchText: String,
            callback: BaseCallback<News.ListResponse>
    ) {
        callback.onLoading()
        api.getNewsListBySearch(
                next = next,
                seachText = searchText
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

    fun getNewsDetail(
            newsShortCode: Long,
            callback: BaseCallback<News.DetailResponse>
    ) {

        callback.onLoading()
        api.getNewsDetail(
                shortCode = newsShortCode
        ).enqueue(object : Callback<News.DetailResponse> {
            override fun onFailure(call: Call<News.DetailResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<News.DetailResponse>, response: Response<News.DetailResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

}