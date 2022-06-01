package uz.xabardor.rest.models.news

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import uz.xabardor.database.NewsDatabase
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.content.Content
import uz.xabardor.rest.models.rubric.Rubric
import uz.xabardor.rest.models.rubric.RubricsData
import java.io.Serializable
import java.util.*

data class News(
    val id: Long,
    val title: String?,
    val title_cyrl: String?,
    val title_ru: String?,
    val slug: String?,
    val short_url: String?,
    val anons: String?,
    val anons_cyrl: String?,
    val anons_ru: String?,
    val photo: String?,
    val photo_source: String?,
    val photo_source_cyrl: String?,
    val photo_source_ru: String?,
    val thumb: String?,
    val content: List<Content>?,
    val content_cyrl: List<Content>?,
    val content_ru: String?,
    val author: String?,
    val is_main: Boolean?,
    val is_photogallery: Boolean?,
    val views: Int?,
    val created_at: String?,
    val published_at: String?,
    val updated_at: String?,
    val is_published: Boolean?,
//    val rubric: RubricsData?,
    val tags: List<Tag>?,
    val isSelected: Boolean?,
    val showDesc: Boolean?,
    val description: String?,
    val description_cyrl: String?,
    val isAds: Boolean?,
    val published: Date?,
    val impressions: Int?,
    val shortCode: Long,
    val shareLink: String?,
    val image: Image,
    val commentInfo: CommentInfo?
//    @SerializedName("image")
//    val image: Image,
//    @SerializedName("commentInfo")
//    val commentInfo: CommentInfo,
//    @SerializedName("isSelected")
//    val isSelected: Boolean = false,
//    @SerializedName("showDesc")
//    val showDesc: Boolean = false,
//    @SerializedName("description")
//    val description: String = "",
//    @SerializedName("isAds")
//    val isAds: Boolean = false,
//    @SerializedName("published")
//    val published: String = "",
//    @SerializedName("impressions")
//    val impressions: Int = 0,
//    @SerializedName("title")
//    val title: String = "",
//    @SerializedName("shortCode")
//    val shortCode: Long = 0,
//    @SerializedName("tags")
//    val tags: List<Tag>?,
//    @SerializedName("shareLink")
//    val shareLink: String = ""
) : Serializable {

    var isTopBanner = false

//    val date: String
//        get() {
//            val p = published
//            lg("published->"+published)
//            var formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
//            val date = formatter.parse(p)
//            formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
//            formatter.timeZone = TimeZone.getTimeZone("GMT")
//            return formatter.format(date)
//        }

    val isFavourite: Boolean
        get() {
            return NewsDatabase.favourites.find {
                it.shortCode == this.shortCode
            } != null
        }

    fun addToFavourite() {
        var favourites = NewsDatabase.favourites
        favourites = favourites.plus(this)
        NewsDatabase.favourites = favourites
    }

    fun removeFromFavourite() {
        var favourites = NewsDatabase.favourites
        favourites = favourites.filter { it.shortCode != this.shortCode }
        NewsDatabase.favourites = favourites
    }


    class ListResponse(
        @SerializedName("pages")
        var pageInfo: PageInfo?,
        var data: Data?,
        var total_pages: Long?
    ) : Serializable {


        class Data(
            @SerializedName("newsList")
            var newsList: List<News>?,
        ) : Serializable


        class PageInfo(
            @SerializedName("prev")
            var prev: Int,
            @SerializedName("next")
            var nextPage: Long?
        ) : Serializable
    }


    class DetailResponse(
        @SerializedName("news")
        var news: News?,
        @SerializedName("related_news")
        var related: List<News>?,
    ) : Serializable {
//        data class News(
//            val id: Long,
//            val title: String?,
//            val title_cyrl: String?,
//            val title_ru: String?,
//            val slug: String?,
//            val short_url: String?,
//            val anons: String?,
//            val anons_cyrl: String?,
//            val anons_ru: String?,
//            val photo: String?,
//            val photo_source: String?,
//            val photo_source_cyrl: String?,
//            val photo_source_ru: String?,
//            val thumb: String?,
//            val content: String?,
//            val content_cyrl: String?,
//            val content_ru: String?,
//            val author: String?,
//            val is_main: Boolean?,
//            val is_photogallery: Boolean?,
//            val views: Int?,
//            val created_at: String?,
//            val published_at: String?,
//            val updated_at: String?,
//            val is_published: Boolean?,
//            val rubric: String?,
//            val tags: List<Tag>?,
//            val isSelected: Boolean?,
//            val showDesc: Boolean?,
//            val description: String?,
//            val isAds: Boolean?,
//            val published: String?,
//            val impressions: Int?,
//            val shortCode: Int?,
//            val shareLink: String?,
//            val image: Image?,
//            val commentInfo: CommentInfo?
//        )
    }


    companion object {
        val topBanner: News
            get() {
                val str =
                    "{ \"title\": \"\", \"description\": \"\", \"image\": { \"orginal\": \"\", \"thumb\": \"\", \"caption\": \"\", \"show\": true, \"showList\": true }, \"published\": \"2019-08-10T22:20:24+05:00\", \"impressions\": 4, \"isAds\": false, \"isSelected\": false, \"showDesc\": true, \"commentInfo\": { \"show\": false, \"count\": 0, \"allow\": false }, \"tags\": [ { \"id\": 108, \"slug\": \"xorij\", \"title\": \"Xorij\" } ], \"shortCode\": 466683 }"
                val news = Gson().fromJson(str, News::class.java)
                news.isTopBanner = true
                return news
            }

        val bottomBanner: News
            get() {
                val str =
                    "{ \"title\": \"\", \"description\": \"\", \"image\": { \"orginal\": \"\", \"thumb\": \"\", \"caption\": \"\", \"show\": true, \"showList\": true }, \"published\": \"2019-08-10T22:20:24+05:00\", \"impressions\": 4, \"isAds\": false, \"isSelected\": false, \"showDesc\": true, \"commentInfo\": { \"show\": false, \"count\": 0, \"allow\": false }, \"tags\": [ { \"id\": 108, \"slug\": \"xorij\", \"title\": \"Xorij\" } ], \"shortCode\": 466683 }"
                val news = Gson().fromJson(str, News::class.java)
                return news
            }
    }

}

