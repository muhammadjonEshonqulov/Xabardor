package uz.xabardor.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.xabardor.rest.models.Tag

object TagDatabase : BaseDatabase(DATABASE_TAGS) {


    private val TAGS = "main_tags"

    var mainTags: List<Tag>
        get() {
            val str = NewsDatabase.preferences.getString(TAGS, "[]")

            val listType = object : TypeToken<List<Tag>>() {}.type
            val list = Gson().fromJson<List<Tag>>(str, listType)
            return list
        }
        set(value) {
            NewsDatabase.preferences.edit().putString(TAGS, Gson().toJson(value)).commit()
        }

}