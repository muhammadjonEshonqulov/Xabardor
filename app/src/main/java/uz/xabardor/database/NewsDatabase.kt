package uz.xabardor.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.xabardor.rest.models.news.News

object NewsDatabase : BaseDatabase(DATABASE_NEWS) {

    private val FAVOURITES = "favourites"

    var favourites: List<News>
        get() {
            val str = preferences.getString(FAVOURITES, "[]")

            val listType = object : TypeToken<List<News>>() {}.type
            val list = Gson().fromJson<List<News>>(str, listType)
            return list
        }
        set(value) {
            preferences.edit().putString(FAVOURITES, Gson().toJson(value)).commit()
        }

}