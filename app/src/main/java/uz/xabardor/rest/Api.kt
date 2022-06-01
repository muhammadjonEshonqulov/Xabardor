package uz.xabardor.rest

import uz.xabardor.rest.models.WeathersAppResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.xabardor.rest.models.AboutResponse
import uz.xabardor.rest.models.Adsense
import uz.xabardor.rest.models.ExchangeRatesData
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.models.rubric.RubricsResponse

interface Api {

    @GET("news/list")
    fun getNewsList(
        @Query("next") next: Long? = null,
        @Query("size") size: Int? = 10,
        @Query("f") type: String? = null,
        @Query("v") tag: String? = null
    ): Call<News.ListResponse>

    @GET("ad/top")
    fun getAdTop(): Call<List<Adsense>>

    @GET("ad/after_trend")
    fun getAfterTrend(): Call<List<Adsense>>

    @GET("ad/after_post")
    fun getAfterPost(): Call<List<Adsense>>

    @GET("exchange-rates")
    fun getExchangeRates(): Call<List<ExchangeRatesData>>

    @GET("weather")
    fun getWeather(): Call<WeathersAppResponse>

    @GET("info/about")
    fun getAbout(): Call<List<AboutResponse>>

    @GET("info/contact")
    fun getContact(): Call<List<AboutResponse>>

    @GET("ad/center")
    fun getAdCenter(): Call<List<Adsense>>

    @GET("news/search")
    fun getNewsListBySearch(
        @Query("next") next: Long? = null,
        @Query("size") size: Int? = 10,
        @Query("search_text") seachText: String? = null
    ): Call<News.ListResponse>

    @GET("news/{shortCode}/info")
    fun getNewsDetail(
        @Path("shortCode") shortCode: Long
    ): Call<News.DetailResponse>


    @GET("tags/list?f=menu")
    fun getMainTagList(): Call<Tag.ListResponse>

    @GET("rubrics/list")
    fun getRubrics(): Call<RubricsResponse>


    //author

    @GET("author/{nick_name}/info")
    fun getAuthorInfo(
        @Path("nick_name") nickName: String
    ): Call<Author.DetailResponse>


    @GET("author/{nick_name}/articles")
    fun getAuthorNewsList(
        @Path("nick_name") nickName: String,
        @Query("f") type: String? = null,
        @Query("v") tag: String? = null,
        @Query("next") next: Long? = null
    ): Call<News.ListResponse>

    //author


}