package uz.tima.xabardor.database

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

open class BaseDatabase(databaseName: String) {

    companion object {
        lateinit var context: Context


        val DATABASE_NEWS = "dabase_news"
        val DATABASE_TAGS = "dabase_tags"
    }

    var preferences: SharedPreferences;
    var gson: Gson

    init {
        gson = Gson()
        preferences = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE)
    }

}