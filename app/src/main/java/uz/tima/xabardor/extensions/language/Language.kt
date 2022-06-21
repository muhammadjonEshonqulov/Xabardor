package uz.tima.xabardor.extensions.language

abstract class Language {
    abstract val id: Int
    abstract val userName: String
    abstract val name:String

    companion object{
        const val UZ = 1
        const val KR = 2

        fun  getNameByLanguage(uz:String?, kr:String?, language: Language):String?{
            return when(language.id){
                UZ -> uz ?: ""
                KR -> kr ?: (uz ?: "")
                else -> null
            }
        }
    }
    
    
}