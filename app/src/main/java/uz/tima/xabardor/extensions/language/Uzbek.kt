package uz.tima.xabardor.extensions.language

import uz.tima.xabardor.extensions.language.Language

class Uzbek : Language() {
    override val id: Int
        get() = Language.UZ
    override val userName: String
        get() = "uz"
    override val name: String
        get() = "O'zbekcha"
}