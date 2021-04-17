package uz.xabardor.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News

interface Api {

    @GET("articles/list")
    fun getNewsList(
            @Query("next") next: Long? = null,
            @Query("f") type: String? = null,
            @Query("v") tag: String? = null
    ): Call<News.ListResponse>

    @GET("articles/search")
    fun getNewsListBySearch(
            @Query("next") next: Long? = null,
            @Query("q") seachText: String? = null
    ): Call<News.ListResponse>

    @GET("articles/{shortCode}/info")
    fun getNewsDetail(
            @Path("shortCode") shortCode: Long
    ): Call<News.DetailResponse>


    @GET("tags/list?f=menu")
    fun getMainTagList(): Call<Tag.ListResponse>


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