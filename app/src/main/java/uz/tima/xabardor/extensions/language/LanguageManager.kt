package uz.tima.xabardor.extensions.language

import android.content.Context
import uz.tima.xabardor.extensions.Prefss

class LanguageManager(val context: Context) {

    var languages: ArrayList<Language> = ArrayList()
    val prefss = Prefss(context)

    init {
        languages.add(
            Uzbek()
        )
        languages.add(
            Krill()
        )
    }

    var currentLanguage: Language
        get() = findLanguageById(prefss.get(prefss.language, languages[0].id))
        set(value) = prefss.save(prefss.language, value.id)

    fun findLanguageById(id: Int): Language {
        languages.forEach {
            if (it.id == id)
                return it
        }
        return languages[0]
    }

}