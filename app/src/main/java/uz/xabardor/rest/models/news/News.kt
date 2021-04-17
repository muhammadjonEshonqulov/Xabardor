package uz.xabardor.rest.models.news

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import uz.xabardor.database.NewsDatabase
import uz.xabardor.rest.models.Tag
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class News(
        @SerializedName("image")
        val image: Image,
        @SerializedName("commentInfo")
        val commentInfo: CommentInfo,
        @SerializedName("isSelected")
        val isSelected: Boolean = false,
        @SerializedName("showDesc")
        val showDesc: Boolean = false,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("isAds")
        val isAds: Boolean = false,
        @SerializedName("published")
        val published: String = "",
        @SerializedName("impressions")
        val impressions: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("shortCode")
        val shortCode: Long = 0,
        @SerializedName("tags")
        val tags: List<Tag>?,
        @SerializedName("shareLink")
        val shareLink: String = ""
) : Serializable {

    var isTopBanner = false

    val date: String
        get() {
            val p = published
            var formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
            val date = formatter.parse(p)
            formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
            formatter.timeZone = TimeZone.getTimeZone("GMT")
            return formatter.format(date)
        }


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
            var data: Data
    ) : Serializable {


        class Data(
                @SerializedName("articles")
                var newsList: List<News>,
                @SerializedName("pageInfo")
                var pageInfo: PageInfo
        ) : Serializable


        class PageInfo(
                @SerializedName("totalCount")
                var totalCount: Int,
                @SerializedName("nextPage")
                var nextPage: Long?
        ) : Serializable
    }


    class DetailResponse(
            var data: Data
    ) : Serializable {


        class Data(
                @SerializedName("related")
                var related: List<News>,
                @SerializedName("info")
                var info: News,
                @SerializedName("author")
                var author: Author
        ) : Serializable
    }


    companion object {
        val topBanner: News
            get() {
                val str = "{ \"title\": \"\", \"description\": \"\", \"image\": { \"orginal\": \"\", \"thumb\": \"\", \"caption\": \"\", \"show\": true, \"showList\": true }, \"published\": \"2019-08-10T22:20:24+05:00\", \"impressions\": 4, \"isAds\": false, \"isSelected\": false, \"showDesc\": true, \"commentInfo\": { \"show\": false, \"count\": 0, \"allow\": false }, \"tags\": [ { \"id\": 108, \"slug\": \"xorij\", \"title\": \"Xorij\" } ], \"shortCode\": 466683 }"
                val news = Gson().fromJson(str, News::class.java)
                news.isTopBanner = true
                return news
            }

        val bottomBanner: News
            get() {
                val str = "{ \"title\": \"\", \"description\": \"\", \"image\": { \"orginal\": \"\", \"thumb\": \"\", \"caption\": \"\", \"show\": true, \"showList\": true }, \"published\": \"2019-08-10T22:20:24+05:00\", \"impressions\": 4, \"isAds\": false, \"isSelected\": false, \"showDesc\": true, \"commentInfo\": { \"show\": false, \"count\": 0, \"allow\": false }, \"tags\": [ { \"id\": 108, \"slug\": \"xorij\", \"title\": \"Xorij\" } ], \"shortCode\": 466683 }"
                val news = Gson().fromJson(str, News::class.java)
                return news
            }
    }

}