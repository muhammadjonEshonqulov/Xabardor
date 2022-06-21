package uz.tima.xabardor.database

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.tima.xabardor.rest.models.Tag
import uz.tima.xabardor.rest.models.rubric.RubricsData

@SuppressLint("StaticFieldLeak")
object TagDatabase : BaseDatabase(DATABASE_TAGS) {


    private val TAGS = "main_tags"
    private val TAGSRUBRUCS = "main_rubrics"

    var mainTags: List<Tag>
        get() {
            val str = NewsDatabase.preferences.getString(TAGS, "[]")

            val listType = object : TypeToken<List<Tag>>() {}.type
            val list = Gson().fromJson<List<Tag>>(str, listType)
            return list
        }
        set(value) {
            NewsDatabase.preferences.edit().putString(TAGS, Gson().toJson(value)).apply()
        }

    var mainRubrics: List<RubricsData>
        get() {
            val str = NewsDatabase.preferences.getString(TAGSRUBRUCS, "[]")

            val listType = object : TypeToken<List<RubricsData>>() {}.type
            val list = Gson().fromJson<List<RubricsData>>(str, listType)
            return list
        }
        set(value) {
            NewsDatabase.preferences.edit().putString(TAGSRUBRUCS, Gson().toJson(value)).apply()
        }

}