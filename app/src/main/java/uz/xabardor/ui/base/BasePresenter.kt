package uz.xabardor.ui.base

import moxy.MvpPresenter
import moxy.MvpView

open class BasePresenter<T : MvpView> : MvpPresenter<T>() {
}