package uz.xabardor.rest.services

import uz.xabardor.rest.models.WeathersAppResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.AboutResponse
import uz.xabardor.rest.models.Adsense
import uz.xabardor.rest.models.ExchangeRatesData
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.models.rubric.RubricsResponse

object NewsService : BaseService() {

    fun getNewsList(
        next: Long? = null,
        callback: BaseCallback<News.ListResponse>
    ) {
        callback.onLoading()
        api.getNewsList(
        ).enqueue(object : Callback<News.ListResponse> {
            override fun onFailure(call: Call<News.ListResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<News.ListResponse>,
                response: Response<News.ListResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getAbout(
        callback: BaseCallback<List<AboutResponse>>
    ) {
        callback.onLoading()
        api.getAbout().enqueue(object : Callback<List<AboutResponse>> {
            override fun onFailure(call: Call<List<AboutResponse>>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<List<AboutResponse>>,
                response: Response<List<AboutResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getContact(
        callback: BaseCallback<List<AboutResponse>>
    ) {
        callback.onLoading()
        api.getContact().enqueue(object : Callback<List<AboutResponse>> {
            override fun onFailure(call: Call<List<AboutResponse>>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<List<AboutResponse>>,
                response: Response<List<AboutResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getAdTop(
        callback: BaseCallback<List<Adsense>>
    ) {
        callback.onLoading()
        api.getAdTop().enqueue(object : Callback<List<Adsense>> {
            override fun onFailure(call: Call<List<Adsense>>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<List<Adsense>>,
                response: Response<List<Adsense>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getAfterPost(
        callback: BaseCallback<List<Adsense>>
    ) {
        callback.onLoading()
        api.getAfterPost().enqueue(object : Callback<List<Adsense>> {
            override fun onFailure(call: Call<List<Adsense>>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<List<Adsense>>,
                response: Response<List<Adsense>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getExchangeRates(
        callback: BaseCallback<List<ExchangeRatesData>>
    ) {
        callback.onLoading()
        api.getExchangeRates().enqueue(object : Callback<List<ExchangeRatesData>> {
            override fun onFailure(call: Call<List<ExchangeRatesData>>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<List<ExchangeRatesData>>,
                response: Response<List<ExchangeRatesData>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getWeather(
        callback: BaseCallback<WeathersAppResponse>
    ) {
        callback.onLoading()
        api.getWeather().enqueue(object : Callback<WeathersAppResponse> {
            override fun onFailure(call: Call<WeathersAppResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<WeathersAppResponse>,
                response: Response<WeathersAppResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getAdCenter(
        callback: BaseCallback<List<Adsense>>
    ) {
        callback.onLoading()
        api.getAdCenter().enqueue(object : Callback<List<Adsense>> {
            override fun onFailure(call: Call<List<Adsense>>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<List<Adsense>>,
                response: Response<List<Adsense>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }
    fun getAfterTrend(
        callback: BaseCallback<List<Adsense>>
    ) {
        callback.onLoading()
        api.getAfterTrend().enqueue(object : Callback<List<Adsense>> {
            override fun onFailure(call: Call<List<Adsense>>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<List<Adsense>>,
                response: Response<List<Adsense>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }


    fun getLastNewsList(
        type: String,
        tag: String,
        next: Long? = null,
        size: Int? = null,
        callback: BaseCallback<News.ListResponse>
    ) {
        callback.onLoading()
        api.getNewsList(
            type = type,
            size = size,
            tag = tag,
            next = next,
        ).enqueue(object : Callback<News.ListResponse> {
            override fun onFailure(call: Call<News.ListResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<News.ListResponse>,
                response: Response<News.ListResponse>
            ) {
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

            override fun onResponse(
                call: Call<News.ListResponse>,
                response: Response<News.ListResponse>
            ) {
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

            override fun onResponse(
                call: Call<News.ListResponse>,
                response: Response<News.ListResponse>
            ) {
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
        type: String? = null,
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

            override fun onResponse(
                call: Call<News.ListResponse>,
                response: Response<News.ListResponse>
            ) {
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

            override fun onResponse(
                call: Call<News.ListResponse>,
                response: Response<News.ListResponse>
            ) {
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

            override fun onResponse(
                call: Call<News.DetailResponse>,
                response: Response<News.DetailResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

    fun getRubrics(
        callback: BaseCallback<RubricsResponse>
    ) {

        callback.onLoading()
        api.getRubrics().enqueue(object : Callback<RubricsResponse> {
            override fun onFailure(call: Call<RubricsResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(
                call: Call<RubricsResponse>,
                response: Response<RubricsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }
}